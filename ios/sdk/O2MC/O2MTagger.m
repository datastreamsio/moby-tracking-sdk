//
//  O2MTagger.m
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import "O2MTagger.h"
#import "O2MDispatcher.h"
#import "O2MUtil.h"

@implementation O2MTagger

-(O2MTagger *) init :(NSString *)endpoint :(NSNumber *)dispatchInterval; {
    self = [super init];
    
    _eventManager = [O2MEventManager sharedManager];
    _dispatcher = [[O2MDispatcher alloc] init :[[NSBundle mainBundle] bundleIdentifier]];
    _alias = [[NSUUID UUID] UUIDString];
    _identity = @"";
    _logTopic = os_log_create("io.o2mc.sdk", "tagger");

    _endpoint = endpoint;
    _dispatchInterval = dispatchInterval;

    _tagQueue = dispatch_queue_create("tagQueue", DISPATCH_QUEUE_SERIAL);
    _dispatchTimer = [NSTimer timerWithTimeInterval:dispatchInterval.floatValue
                                             target:self
                                           selector:@selector(dispatch:)
                                           userInfo:nil
                                            repeats:YES];
    [NSRunLoop.mainRunLoop addTimer:_dispatchTimer forMode:NSRunLoopCommonModes];

    return self;
}

#pragma mark - Configuration methods

-(void) setMaxRetries :(NSInteger)maxRetries; {
    if (_dispatcher) {
        [_dispatcher setConnRetriesMax: maxRetries];
    }
}

#pragma mark - Control methods

-(void) clearFunnel; {
    dispatch_async(_tagQueue, ^{
      [self->_eventManager clearEvents];
    });
}

-(void)stop {
    [self stop:YES];
}

-(void)stop:(BOOL) clearFunnel; {
    dispatch_async(_tagQueue, ^{
        os_log_info(self->_logTopic, "stopping tracking");
        [self->_dispatchTimer invalidate];

        if (clearFunnel == YES) {
            #ifdef DEBUG
                os_log_debug(self->_logTopic, "clearing the funnel");
            #endif
            [self clearFunnel];
        }
    });
}

#pragma mark - Tracking methods

-(void)track :(NSString*)eventName; {
    dispatch_async(_tagQueue, ^{
        if (!self->_dispatchTimer.isValid) return;
        #ifdef DEBUG
            os_log_debug(self->_logTopic, "Track %@", eventName);
        #endif

        [self->_eventManager addEvent:@{
                                        @"event" : eventName,
                                        @"alias":self->_alias,
                                        @"identitiy":self->_identity,
                                        @"time":[O2MUtil currentTimestamp]
                                        }];
    });
}

-(void)trackWithProperties:(NSString*)eventName :(NSString*)propertiesAsJson;
{
    dispatch_async(_tagQueue, ^{
        if (!self->_dispatchTimer.isValid) return;
        #ifdef DEBUG
            os_log_debug(self->_logTopic, "Track %@:%@", eventName, propertiesAsJson);
        #endif

        [self->_eventManager addEvent:@{
                                        @"event" : eventName,
                                        @"alias":self->_alias,
                                        @"identitiy":self->_identity,
                                        @"time":[O2MUtil currentTimestamp],
                                        @"properties":propertiesAsJson
                                        }];
    });
}

#pragma mark - Internal methods

-(void) dispatch:(NSTimer *)timer;{
    dispatch_async(_tagQueue, ^{
        if(self->_eventManager.events.count > 0){
            if(self->_dispatcher.connRetries < self->_dispatcher.connRetriesMax) {
                #ifdef DEBUG
                    os_log_debug(self->_logTopic, "Dispatcher has been triggered");
                #endif
                [self->_dispatcher dispatch :self->_endpoint :self->_eventManager.events];
            } else {
                os_log_info(self->_logTopic, "Reached max connection retries (%ld), stopping dispatcher.", (long)self->_dispatcher.connRetriesMax);

                // Stopping the time based interval loop.
                [timer invalidate];
            }
        }
    });
}

@end



