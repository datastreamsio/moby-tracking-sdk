//
//  O2MBatchManager.m
//  datastreams
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MBatchManager.h"

@implementation O2MBatchManager

- (instancetype) init {
    if (self = [super init]) {
        _batchQueue = dispatch_queue_create("batchQueue", DISPATCH_QUEUE_SERIAL);
        _connRetries = 0;
        _dispatcher = [[O2MDispatcher alloc] init :[[NSBundle mainBundle] bundleIdentifier]];
        _eventManager = [O2MEventManager sharedManager];

        _logTopic = os_log_create("io.o2mc.sdk", "batchmanager");

        // Handle dispatcher's callbacks
        _dispatcher.delegate = self;
    }
    return self;
}

+ (instancetype)sharedManager {
    static O2MBatchManager *sharedO2MBatchManager = nil;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        sharedO2MBatchManager = [[self alloc] init];
    });
    return sharedO2MBatchManager;
}

-(void) startTimer :(NSNumber *) dispatchInterval; {
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

-(void) dispatch:(NSTimer *)timer;{
    dispatch_async(_batchQueue, ^{
        if(self->_eventManager.events.count > 0){
            if(self->_connRetries < self->_maxRetries) {
                #ifdef DEBUG
                    os_log_debug(self->_logTopic, "Dispatcher has been triggered");
                #endif
                [self->_dispatcher dispatch :self->_endpoint :self->_eventManager.events :self->_connRetries];
            } else {
                os_log_info(self->_logTopic, "Reached max connection retries (%ld), stopping dispatcher.", (long)self->_maxRetries);

                // Stopping the time based interval loop.
                [self stop];
            }
        }
    });
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
    #ifdef DEBUG
        os_log_debug(self->_logTopic, "Dispatcher error");
    #endif
    self->_connRetries++;
}
- (void)didDispatchWithSuccess:(id)sender; {
    self->_batchNumber++;
    self->_connRetries = 0;
}



@end
