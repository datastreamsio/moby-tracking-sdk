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
    return self;
}

- (NSMutableDictionary *)getGeneralInfo; {

    Device *d = [[Device alloc] init];

    NSMutableDictionary *data = @{
            @"AppId" : _appName,
            @"ip" : [self getIPAddress],
            @"connection" : @"3G",
            @"os": [[UIDevice currentDevice] systemName],
            @"osVersion": [[UIDevice currentDevice] systemVersion],
            @"device":d.deviceName
    };

    return data;
}

- (void)dispatch:(NSString *)endpoint :(NSMutableDictionary *)funnel; {
    NSLog(@"Am i being called?");
    
    NSDictionary *data = @{
            @"application" :  [self getGeneralInfo],
            @"tracked" : funnel
    };


    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:data
                                                       options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                         error:&error];

    if (!jsonData) {
        NSLog(@"Got an error: %@", error);
    } else {
        NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
        NSString *post = [NSString stringWithFormat:@"%@", jsonString];
        NSData *postData = [post dataUsingEncoding:NSASCIIStringEncoding allowLossyConversion:YES];
        NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
        [request setURL:[NSURL URLWithString:endpoint]];
        [request setHTTPMethod:@"POST"];
        [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
        [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
        [request setHTTPBody:postData];
        NSURLConnection *conn = [[NSURLConnection alloc] initWithRequest:request delegate:self];
        if (conn) {
            NSLog(@"length (%@) Funnel -> ( %@ ) has been dispatched to: %@", postLength, jsonString, endpoint);
            [funnel removeAllObjects];
        } else {
            NSLog(@"Connection could not be made");
        }
    }

}


- (NSString *)getIPAddress {
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while (temp_addr != NULL) {
            if (temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if ([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    address = [NSString stringWithUTF8String:inet_ntoa(((struct sockaddr_in *) temp_addr->ifa_addr)->sin_addr)];

                }

            }

            temp_addr = temp_addr->ifa_next;
        }
    }
    // Free memory
    freeifaddrs(interfaces);
    return address;
}


@end