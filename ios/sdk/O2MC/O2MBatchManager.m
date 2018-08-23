//
//  O2MBatchManager.m
//  datastreams
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MBatchManager.h"

#import "O2MBatch.h"
#import "O2MDispatcher.h"
#import "O2MEventManager.h"
#import "O2MLogger.h"
#import "O2MUtil.h"
#import <UIKit/UIDevice.h>


@interface O2MBatchManager()

@property NSMutableArray *batches;
@property int batchNumber;
@property (nonatomic, readonly, strong) dispatch_queue_t batchQueue;
@property (assign, nonatomic, readonly) NSInteger connRetries;
@property (readonly) NSDictionary *deviceInfo;
@property O2MDispatcher *dispatcher;
@property NSTimer * dispatchTimer;
@property O2MEventManager *eventManager;
@property (readonly) O2MLogger *logger;

@end

@implementation O2MBatchManager

- (instancetype) init {
    if (self = [super init]) {
        _batches = [[NSMutableArray alloc] init];
        _batchQueue = dispatch_queue_create("io.o2mc.sdk", DISPATCH_QUEUE_SERIAL);
        _connRetries = 0;
        _deviceInfo = @{
                        @"appId": [[NSBundle mainBundle] bundleIdentifier],
                        @"locale": [[NSLocale currentLocale] localeIdentifier],
                        @"name": [O2MUtil deviceName],
                        @"os": UIDevice.currentDevice.systemName,
                        @"osVersion":UIDevice.currentDevice.systemVersion,
                        };
        _dispatcher = [[O2MDispatcher alloc] init :[[NSBundle mainBundle] bundleIdentifier]];
        _endpoint = @"";
        _eventManager = [O2MEventManager sharedManager];

        _logger = [[O2MLogger alloc] initWithTopic:"batchmanager"];

        // Handle dispatcher's callbacks
        _dispatcher.delegate = self;
    }
    return self;
}

-(void) dispatchWithInterval :(NSNumber *) dispatchInterval; {
    dispatch_async(_batchQueue, ^{
        if (self->_dispatchTimer) {
            [self->_dispatchTimer invalidate];
            self->_dispatchTimer = nil;
        }
        self->_dispatchTimer = [NSTimer timerWithTimeInterval:[dispatchInterval floatValue] target:self selector:@selector(dispatch:) userInfo:nil repeats:YES];

        // Start the dispatch timer
        [NSRunLoop.mainRunLoop addTimer:self->_dispatchTimer forMode:NSRunLoopCommonModes];
    });
}

-(void) createBatch; {
    dispatch_async(self.batchQueue, ^{
        O2MBatch *batch = [[O2MBatch alloc] initWithParams:self->_deviceInfo :self->_batchNumber];

        int i;
        for (i=0; i< self->_eventManager.events.count; i++) {
            [batch addEvent:self->_eventManager.events[i]];
        }

        [self->_batches addObject:batch];
        [self->_eventManager.events removeAllObjects];
    });
}

-(void) batchRetryIncrement; {
    dispatch_async(self.batchQueue, ^{
        if(self->_batches.count > 0) {
            [[self->_batches objectAtIndex:0] addRetry];
        }
    });
}

-(void) dispatch:(NSTimer *)timer;{
    dispatch_async(_batchQueue, ^{
        if(self->_batches.count > 0){
            if(self->_connRetries < self->_maxRetries) {
                [self->_logger logD:@"Dispatcher has been triggered"];
                [self->_dispatcher dispatchWithEndpoint:self->_endpoint batch:self->_batches[0] sessionId:self->_sessionIdentifier];
            } else {
                [self->_logger logI:@"Reached max connection retries (%ld), stopping dispatcher.", (long)self->_maxRetries];

                // Stopping the time based interval loop.
                [self stop];
            }
        } else if([self->_eventManager eventCount] > 0) {
            [self createBatch];
        }
    });
}

-(BOOL) isDispatching; {
    return [self->_dispatchTimer isValid];
}

-(void) stop; {
    dispatch_async(_batchQueue, ^{
        if (self->_dispatchTimer) {
            [self->_dispatchTimer invalidate];
            self->_dispatchTimer = nil;
        }
    });
}

- (void)didDispatchWithError:(id)sender; {
    dispatch_async(_batchQueue, ^{
        [self->_logger logD:@"Dispatcher error"];
        self->_connRetries++;
        [self batchRetryIncrement];
    });
}
- (void)didDispatchWithSuccess:(id)sender; {
    self->_batchNumber++;
    self->_connRetries = 0;

    [self->_batches removeAllObjects]; // TODO: should remove a specific batch in case there are more items.
}



@end
