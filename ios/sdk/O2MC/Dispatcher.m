//
// Created by Nicky Romeijn on 23-06-16.
// Copyright (c) 2016 Adversitement. All rights reserved.
//


#import "Dispatcher.h"
#import "Device.h"
#import <UIKit/UIDevice.h>
#include <ifaddrs.h>
#include <arpa/inet.h>


@implementation Dispatcher {

}

- (id)init:(NSString *)appName; {
    self = [super init];
    _appName = appName;
    _logTopic = os_log_create("io.o2mc.sdk", "dispatcher");
    return self;
}

- (NSMutableDictionary *)getGeneralInfo; {

    Device *d = [[Device alloc] init];

    NSMutableDictionary* data = [NSMutableDictionary new];
    [data setObject:_appName forKey:@"AppId"];
    [data setObject:@"3G" forKey:@"connection"];
    [data setObject:[[UIDevice currentDevice] systemName] forKey:@"os"];
    [data setObject:[[UIDevice currentDevice] systemVersion] forKey:@"osVersion"];
    [data setObject:d.deviceName forKey:@"device"];

    return data;
}

- (void)dispatch:(NSString *)endpoint :(NSMutableDictionary *)funnel; {
    NSDictionary *data = @{
            @"application" :  [self getGeneralInfo],
            @"tracked" : funnel
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

            if ([data length] > 0 && error == nil) {
                if ([httpResponse statusCode] == 200 || [httpResponse statusCode] == 201) {
                    os_log(self->_logTopic, "length (%lu) Funnel -> ( %@ ) has been dispatched to: %@", (unsigned long)[data length], jsonString, [response URL]);
                    [funnel removeAllObjects];
                }
            } else {
                os_log(self->_logTopic, "Connection could not be made");
            }
        }];

        [dataTask resume];
    }

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
