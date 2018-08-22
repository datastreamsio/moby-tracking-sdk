//
//  O2MTagger.m
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import "O2MTagger.h"

#import "O2MBatchManager.h"
#import "O2MEventManager.h"
#import "Models/O2MEvent.h"
#import "O2MLogger.h"
#import "O2MUtil.h"

@interface O2MTagger()

@property O2MBatchManager *batchManager;
@property O2MEventManager *eventManager;
@property O2MLogger *logger;
@property dispatch_queue_t tagQueue;

@end

@implementation O2MTagger

-(O2MTagger *) init :(NSString *)endpoint :(NSNumber *)dispatchInterval; {
    self = [super init];
    
    _batchManager = [O2MBatchManager sharedManager];
    _eventManager = [O2MEventManager sharedManager];
    _logger = [[O2MLogger alloc] initWithTopic:"tagger"];

    _tagQueue = dispatch_queue_create("io.o2mc.sdk", DISPATCH_QUEUE_SERIAL);

    [self->_batchManager setEndpoint:endpoint];
    [self->_batchManager dispatchWithInterval:dispatchInterval];

    return self;
}

#pragma mark - Configuration methods
-(NSString*) getEndpoint; {
    return self->_batchManager.endpoint;
}

-(void) setEndpoint :(NSString *) endpoint; {
    [self->_batchManager setEndpoint:endpoint];
}


-(void) setMaxRetries :(NSInteger)maxRetries; {
    [_batchManager setMaxRetries: maxRetries];
}

-(void)setIdentifier :(NSString*) uniqueIdentifier; {
    [self->_batchManager setSessionIdentifier:uniqueIdentifier];
}

-(void)setSessionIdentifier; {
    [self->_batchManager setSessionIdentifier:[[NSUUID UUID] UUIDString]];
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
        [self->_logger logI:@"stopping tracking"];
        [self->_batchManager stop];

        if (clearFunnel == YES) {
            [self->_logger logD:@"clearing the funnel"];
            [self clearFunnel];
        }
    });
}

#pragma mark - Tracking methods

-(void)track :(NSString*)eventName; {
    dispatch_async(_tagQueue, ^{
        if (![self->_batchManager isDispatching]) return;
        [self->_logger logD:@"Track %@", eventName];

        [self->_eventManager addEvent: [[O2MEvent alloc] init:eventName]];
    });
}

-(void)trackWithProperties:(NSString*)eventName properties:(NSDictionary*)properties;
{
    dispatch_async(_tagQueue, ^{
        if (![self->_batchManager isDispatching]) return;
        [self->_logger logD:@"Track %@:%@", eventName, properties];

        [self->_eventManager addEvent: [[O2MEvent alloc] initWithProperties:eventName
                                                                 properties:properties]];
    });
}

@end



