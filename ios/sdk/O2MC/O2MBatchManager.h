//
//  O2MBatchManager.h
//  O2MC
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <os/log.h>
#import "O2MDispatcher.h"
#import "O2MDispatcherDelegate.h"
#import "O2MEventManager.h"

@interface O2MBatchManager : NSObject <O2MDispatcherDelegate>

@property int batchNumber;
@property (nonatomic, readonly, strong) dispatch_queue_t batchQueue;
@property O2MDispatcher *dispatcher;
@property NSTimer * dispatchTimer;
@property NSString *endpoint;
@property O2MEventManager *eventManager;
@property (readonly) os_log_t logTopic;
@property NSInteger maxRetries;


+ (instancetype)sharedManager;
/**
 * Starts the timer with a specific dispatch interval.
 * @param dispatchInterval dispatch time in seconds.
 */
-(void) startTimer :(NSNumber *) dispatchInterval;
-(void) dispatch :(NSTimer *)timer;
-(void) stop;

# pragma mark - Dispatcher delegator methods.

- (void)didDispatchWithError:(id)sender;
- (void)didDispatchWithSuccess:(id)sender;

@end
