//
//  O2MEvent.m
//  datastreams
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MEvent.h"

@implementation O2MEvent

-(instancetype) init :(NSString*) event; {
    if (self = [super init]) {
        _event = event;
        _timestamp = [O2MUtil currentTimestamp];
    }
    return self;
}

-(instancetype) initWithProperties :(NSString*) event :(NSDictionary*) properties; {
    if (self = [super init]) {
        _event = event;
        _properties = properties;
        _timestamp = [O2MUtil currentTimestamp];
    }
    return self;
}

-(NSDictionary*) toDict; {
    if (_properties != nil) {
        return @{
                 @"name": _event,
                 @"value": _properties,
                 @"timestamp": _timestamp
                 };
    }
    return @{
             @"name": _event,
             @"timestamp": _timestamp,
             };
}

@end
