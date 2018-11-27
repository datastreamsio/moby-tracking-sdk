//
//  O2MConfig.m
//  datastreams
//
//  Created by Tim Slot on 04/09/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MConfig.h"


@implementation O2MConfig
static NSNumber *_batchInterval = nil;
static NSNumber *_dispatchInterval = nil;
static NSString *_httpEndpoint = @"http://127.0.0.1:5000/events";
static NSInteger _maxRetries = 2;

+ (NSNumber*)batchInterval {
    if(_batchInterval == nil){
        _batchInterval = [[NSNumber alloc] initWithInt:2];
    }
    return _batchInterval;
}

+ (NSNumber*)dispatchInterval {
    if(_dispatchInterval == nil){
        [O2MConfig setDispatchInterval:[[NSNumber alloc] initWithInt:10]];
    }
    return _dispatchInterval;
}

+ (void)setDispatchInterval:(NSNumber *)dispatchInterval {
    _dispatchInterval = dispatchInterval;
}

+ (NSString*)httpEndpoint {
    return _httpEndpoint;
}

+ (NSInteger)maxRetries {
    return _maxRetries;
}

@end
