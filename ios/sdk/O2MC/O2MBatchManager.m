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
#import "O2MLogger.h"
#import "O2MUtil.h"
#import <UIKit/UIDevice.h>


@interface O2MBatchManager()

@property NSMutableArray *batches;
@property int batchNumber;
@property (nonatomic, readonly, strong) dispatch_queue_t batchQueue;
@property (assign, nonatomic, readwrite) NSInteger connRetries;
@property (readonly) NSDictionary *deviceInfo;
@property O2MDispatcher *dispatcher;
@property NSTimer * dispatchTimer;
@property (readonly) O2MLogger *logger;

@end

@implementation O2MBatchManager

-(instancetype) init; {
    if (self = [super init]) {
        _batches = [[NSMutableArray alloc] init];
        _batchQueue = dispatch_queue_create("io.o2mc.sdk", DISPATCH_QUEUE_SERIAL);
        _connRetries = 0;
        _deviceInfo = @{
                        @"appId": [[NSBundle mainBundle] bundleIdentifier] ?: [NSNull null],
                        @"locale": [[NSLocale currentLocale] localeIdentifier],
                        @"name": [O2MUtil deviceName],
                        @"os": UIDevice.currentDevice.systemName,
                        @"osVersion":UIDevice.currentDevice.systemVersion,
                        };
        _dispatcher = [[O2MDispatcher alloc] init :[[NSBundle mainBundle] bundleIdentifier]];
        _endpoint = @"";

        _logger = [[O2MLogger alloc] initWithTopic:"batchmanager"];

        // Handle dispatcher's callbacks
        _dispatcher.delegate = self;
    }
    return self;
}

-(void) dispatchWithInterval :(NSNumber *) dispatchInterval; {
    if (self->_dispatchTimer) {
        [self->_dispatchTimer invalidate];
        self->_dispatchTimer = nil;
    }
    self->_dispatchTimer = [NSTimer timerWithTimeInterval:[dispatchInterval floatValue] target:self selector:@selector(dispatch:) userInfo:nil repeats:YES];

    // Start the dispatch timer
    [NSRunLoop.mainRunLoop addTimer:self->_dispatchTimer forMode:NSRunLoopCommonModes];

    // Reset connection retries on (re)start
    self->_connRetries = 0;
}

-(void) createBatchWithEvents:(NSArray*)events; {
    dispatch_async(self.batchQueue, ^{
        O2MBatch *batch = [[O2MBatch alloc] initWithParams:self->_deviceInfo :self->_batchNumber];
        self->_batchNumber++;

        for (int i=0; i< events.count; i++) {
            [batch addEvent:events[i]];
        }

        [self->_batches addObject:batch];
    });
}

-(void) batchRetryIncrement; {
    dispatch_async(self.batchQueue, ^{
        if(self->_batches.count > 0) {
            [[self->_batches objectAtIndex:0] addRetry];
        }
    });
}

-(O2MBatch*) mergeBatches; {
    if(self->_batches.count < 1) {
        return nil;
    }

    if(self->_batches.count == 1) {
        return self->_batches[0];
    }
    // We found multiple batches ready, lets merge them all together.
    NSMutableArray* events = [[NSMutableArray alloc] init];
    O2MBatch* batch = self->_batches[0];

    // We start at the latest batch number, move the events and remove the batch.
    for(int i=(int)self->_batches.count-1; i>0; i--) {
        [events addObjectsFromArray:[self->_batches[i] events]];
        [self->_batches removeLastObject];
    }

    [self->_logger logD:@"merged %lu batches into 1", (unsigned long)self->_batches.count];

    return batch;
}

-(void) dispatch:(NSTimer *)timer;{
    dispatch_async(self.batchQueue, ^{
        if(self->_batches.count > 0){
            if(self->_connRetries < self->_maxRetries) {
                [self->_logger logD:@"Dispatcher has been triggered"];
                [self->_dispatcher dispatchWithEndpoint:self->_endpoint
                                                  batch:self->_batches[0]
                                              sessionId:self->_sessionIdentifier];
            } else {
                [self->_logger logI:@"Reached max connection retries (%ld), stopping dispatcher.", (long)self->_maxRetries];

                // Stopping the time based interval loop.
                [self stop];
            }
        }
    });
}

-(BOOL) isDispatching; {
    return [self->_dispatchTimer isValid];
}

-(void) stop; {
    if (self->_dispatchTimer) {
        [self->_dispatchTimer invalidate];
        self->_dispatchTimer = nil;
    }
}

- (void)didDispatchWithError:(id)sender; {
    dispatch_async(self.batchQueue, ^{
        [self->_logger logD:@"Dispatcher error"];
        self->_connRetries++;
        [self batchRetryIncrement];
    });
}
- (void)didDispatchWithSuccess:(id)sender; {
    dispatch_async(self.batchQueue, ^{
        self->_connRetries = 0;
        if([self->_batches count] > 0) {
            [self->_batches removeObjectAtIndex:0];
        }
    });
}



@end
