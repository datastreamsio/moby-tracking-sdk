//
//  O2MEventManager.m
//  datastreams
//
//  Created by Tim Slot on 18/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MEventManager.h"

@implementation O2MEventManager
+ (instancetype)sharedManager {
    static O2MEventManager *sharedO2MEventManager = nil;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        sharedO2MEventManager = [[self alloc] init];
    });
    return sharedO2MEventManager;
}

-(instancetype) init; {
    if (self = [super init]) {
        _events = [[NSMutableArray alloc] init];
        _eventQueue = dispatch_queue_create("io.o2mc.sdk", DISPATCH_QUEUE_SERIAL);
    }
    return self;
}

-(void) addEvent :(O2MEvent*)event; {
    dispatch_async(self->_eventQueue,^{
        [self->_events addObject:event];
    });
}

-(void) clearEvents; {
    dispatch_async(self->_eventQueue, ^{
        [self->_events removeAllObjects];
    });
}

-(long) eventCount; {
    __block long count;

    dispatch_sync(self->_eventQueue, ^{
        count = self->_events.count;
    });

    return count;
}

@end
