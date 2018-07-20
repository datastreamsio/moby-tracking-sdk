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
        _dispatcher = [[O2MDispatcher alloc] init :[[NSBundle mainBundle] bundleIdentifier]];
        _eventManager = [O2MEventManager sharedManager];

        _logTopic = os_log_create("io.o2mc.sdk", "batchmanager");
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
        // TODO: this method should supply the batch number
        if(self->_eventManager.events.count > 0){
            if(self->_dispatcher.connRetries < self->_maxRetries) {
                #ifdef DEBUG
                    os_log_debug(self->_logTopic, "Dispatcher has been triggered");
                #endif
                [self->_dispatcher dispatch :self->_endpoint :self->_eventManager.events];
            } else {
                os_log_info(self->_logTopic, "Reached max connection retries (%ld), stopping dispatcher.", (long)self->_maxRetries);

                // Stopping the time based interval loop.
                [timer invalidate];
            }
        }
    });
}

-(void) stop; {
    dispatch_async(_batchQueue, ^{
        [self->_dispatchTimer invalidate];
    });
}

@end
