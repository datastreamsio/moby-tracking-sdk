//
// Created by Nicky Romeijn on 23-06-16.
// Copyright (c) 2016 Adversitement. All rights reserved.
//


#import "O2MDispatcher.h"
#import "O2MDevice.h"
#import "O2MUtil.h"
#import <UIKit/UIDevice.h>


@implementation O2MDispatcher {

}

- (id)init:(NSString *)appName; {
    self = [super init];
    _appName = appName;
    _logTopic = os_log_create("io.o2mc.sdk", "dispatcher");
    _connRetries = 0;
    _connRetriesMax = 5;
    return self;
}

- (NSMutableDictionary *)getGeneralInfo; {

    O2MDevice *d = [[O2MDevice alloc] init];

    NSMutableDictionary* data = [NSMutableDictionary new];
    [data setObject:_appName forKey:@"appId"];
    [data setObject:UIDevice.currentDevice.systemName forKey:@"os"];
    [data setObject:UIDevice.currentDevice.systemVersion forKey:@"osVersion"];
    [data setObject:d.deviceName forKey:@"deviceName"];

    return data;
}

- (void)dispatch:(NSString *)endpoint :(NSMutableArray *)funnel; {
    NSDictionary *data = @{
            @"deviceInformation" :  [self getGeneralInfo],
            @"events" : funnel,
            @"retries": [NSString stringWithFormat:@"%zd", self->_connRetries],
            @"timestamp": [O2MUtil currentTimestamp]
    };


    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];

    if (!jsonData) {
        os_log_error(self->_logTopic, "Got an error: %@", error);
    } else {
        NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        NSString *post = [NSString stringWithFormat:@"%@", jsonString];
        NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
        NSString *postLength = [NSString stringWithFormat:@"%lu", (unsigned long)[postData length]];
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:endpoint]];
        [request setHTTPMethod:@"POST"];
        [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:postData];

        NSURLSessionDataTask *dataTask = [[self urlSession] dataTaskWithRequest: request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
           NSHTTPURLResponse *httpResponse = (NSHTTPURLResponse *)response;

            if (data.length > 0 && error == nil) {
                if (httpResponse.statusCode == 200 || httpResponse.statusCode == 201) {
                    #ifdef DEBUG
                        os_log_debug(self->_logTopic, "length (%lu) Funnel -> ( %@ ) has been dispatched to: %@", (unsigned long)[data length], jsonString,     [response URL]);
                    #endif
                    [funnel removeAllObjects];
                    [self successHandler];
                }
            } else {
                os_log(self->_logTopic, "Connection could not be made");
                [self errorHandler];
            }
        }];

        [dataTask resume];
    }

}

-(void) successHandler {
    // Reset after each successful data post.
    self->_connRetries = 0;
}

-(void) errorHandler {
    self->_connRetries++;
}

-(NSURLSession *) urlSession {
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        // We use an empheral session config since we don't want to store any
        // data on the device's disk.
        NSURLSessionConfiguration *empheralConfigObject = [NSURLSessionConfiguration ephemeralSessionConfiguration];

        self->_session = [NSURLSession sessionWithConfiguration:empheralConfigObject delegate:nil delegateQueue: [NSOperationQueue mainQueue]];
    });

    return _session;
}



@end
