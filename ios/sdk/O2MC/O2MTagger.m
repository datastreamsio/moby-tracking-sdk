//
//  O2MTagger.m
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import "O2MTagger.h"


@implementation O2MTagger

-(O2MTagger *) init :(NSString *)endpoint :(NSNumber *)dispatchInterval; {
    self = [super init];
    
    _batchManager = [O2MBatchManager sharedManager];
    _eventManager = [O2MEventManager sharedManager];
    _logTopic = os_log_create("io.o2mc.sdk", "tagger");

    _endpoint = endpoint;

    _tagQueue = dispatch_queue_create("io.o2mc.sdk", DISPATCH_QUEUE_SERIAL);

    [self->_batchManager setEndpoint:endpoint];
    [self->_batchManager startTimer:dispatchInterval];

    return self;
}

#pragma mark - Configuration methods
-(void) setEndpoint :(NSString *) endpoint; {
    [self->_batchManager setEndpoint:endpoint];
}


-(void) setMaxRetries :(NSInteger)maxRetries; {
    [_batchManager setMaxRetries: maxRetries];
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
        [self->_batchManager stop];

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
        if (!self->_batchManager.dispatchTimer.isValid) return;
        #ifdef DEBUG
            os_log_debug(self->_logTopic, "Track %@", eventName);
        #endif

        [self->_eventManager addEvent: [[O2MEvent alloc] init:eventName]];
    });
}

-(void)trackWithProperties:(NSString*)eventName :(NSDictionary*)properties;
{
    dispatch_async(_tagQueue, ^{
        if (!self->_batchManager.dispatchTimer.isValid) return;
        #ifdef DEBUG
            os_log_debug(self->_logTopic, "Track %@:%@", eventName, properties);
        #endif

        [self->_eventManager addEvent: [[O2MEvent alloc] initWithProperties:eventName :properties]];
    });
}

@end



