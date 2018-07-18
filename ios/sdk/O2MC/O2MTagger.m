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

static int objectCount = 0;

-(O2MTagger *) init :(NSString *)endpoint :(NSNumber *)dispatchInterval; {
    self = [super init];
    
    _funnel = [[NSMutableArray alloc] init];
    self.funnel_lock = [[NSLock alloc] init];
    _dispatcher = [[O2MDispatcher alloc] init :[[NSBundle mainBundle] bundleIdentifier]];
    _alias = [[NSUUID UUID] UUIDString];
    _identity = @"";
    _logTopic = os_log_create("io.o2mc.sdk", "tagger");

    [self setEndpoint:endpoint];
    [self setDispatchInterval:dispatchInterval];

    _dispatchTimer = [NSTimer timerWithTimeInterval:dispatchInterval.floatValue
                                             target:self
                                           selector:@selector(dispatch:)
                                           userInfo:nil
                                            repeats:YES];
    [[NSRunLoop mainRunLoop] addTimer:_dispatchTimer forMode:NSRunLoopCommonModes];

    objectCount++;
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
    [self.funnel_lock lock];
    [_funnel removeAllObjects];
    [self.funnel_lock unlock];
}

-(void)stop {
    [self stop:YES];
}

-(void)stop:(BOOL) clearFunnel; {
    os_log_info(self->_logTopic, "stopping tracking");
    [_dispatchTimer invalidate];
    
    if (clearFunnel == YES) {
        #ifdef DEBUG
            os_log_debug(self->_logTopic, "clearing the funnel");
        #endif
        [self clearFunnel];
    }
}

#pragma mark - Tracking methods

-(void)track :(NSString*)eventName; {
    if (![_dispatchTimer isValid]) return;
    #ifdef DEBUG
        os_log_debug(self->_logTopic, "Track %@", eventName);
    #endif

    NSDictionary *funnel = @{
                             @"event" : eventName,
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[O2MUtil currentTimestamp]
                             };
    
    [self addToFunnel:eventName :funnel];
}

-(void)trackWithProperties:(NSString*)eventName :(NSString*)propertiesAsJson;
{
    if (![_dispatchTimer isValid]) return;
    #ifdef DEBUG
        os_log_debug(self->_logTopic, "Track %@:%@", eventName, propertiesAsJson);
    #endif

    NSDictionary *funnel = @{
                             @"event" : eventName,
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[O2MUtil currentTimestamp],
                             @"properties":propertiesAsJson
                             };
    [self addToFunnel:eventName :funnel];
}

#pragma mark - Internal methods

-(void) dispatch:(NSTimer *)timer;{
    [self.funnel_lock lock];
    if(_funnel.count > 0){
        if(_dispatcher.connRetries < _dispatcher.connRetriesMax) {
            #ifdef DEBUG
                os_log_debug(self->_logTopic, "Dispatcher has been triggered");
            #endif
            [_dispatcher dispatch :_endpoint :_funnel];
        } else {
            os_log_info(self->_logTopic, "Reached max connection retries (%ld), stopping dispatcher.", (long)_dispatcher.connRetriesMax);

            // Stopping the time based interval loop.
            [timer invalidate];
        }
    }
    [self.funnel_lock unlock];
}

-(void) addToFunnel :(NSString*)funnelKey :(NSDictionary*)funnelData; {
    [self.funnel_lock lock];

    [_funnel addObject:funnelData];

    #ifdef DEBUG
        os_log_debug(self->_logTopic, "number of events %lu", (unsigned long)[_funnel count]);
    #endif
    [self.funnel_lock unlock];
}

@end



