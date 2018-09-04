//
//  O2MConfig.m
//  datastreams
//
//  Created by Tim Slot on 04/09/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MConfig.h"


@implementation O2MConfig
static NSString *_httpEndpoint = @"http://127.0.0.1:5000/events";
static NSInteger _maxRetries = 2;

+ (NSString*)httpEndpoint {
    return _httpEndpoint;
}

+ (NSInteger)maxRetries {
    return _maxRetries;
}

@end
