//
// Created by Nicky Romeijn on 23-06-16.
// Copyright (c) 2016 Adversitement. All rights reserved.
//

#import "O2MDispatcher.h"


@implementation O2MDispatcher {

}

- (id)init:(NSString *)appName; {
    self = [super init];
    _appName = appName;
    _logTopic = os_log_create("io.o2mc.sdk", "dispatcher");
    return self;
}

-(void) dispatch:(NSString*)endpoint :(O2MBatch*)batch; {
    NSDictionary *data = @{
            @"deviceInformation" :  [batch deviceInformation],
            @"events" : [batch eventsAsString],
            @"number": [NSNumber numberWithLong:batch.number],
            @"retries": [NSNumber numberWithLong:batch.retries],
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
    [self.delegate didDispatchWithSuccess:self];
}

-(void) errorHandler {
    [self.delegate didDispatchWithError:self];
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
