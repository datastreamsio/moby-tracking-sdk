//
//  O2MBatchManager.h
//  O2MC
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MBatch.h"
#import "O2MDispatcher.h"
#import "O2MDispatcherDelegate.h"
#import "O2MEventManager.h"
#import "O2MLogger.h"
#import "O2MUtil.h"
#import <UIKit/UIDevice.h>

@interface O2MBatchManager : NSObject <O2MDispatcherDelegate>

@property NSString *endpoint;
@property NSInteger maxRetries;
@property NSString *sessionIdentifier;


+ (instancetype)sharedManager;
/**
 * Starts the timer with a specific dispatch interval.
 * @param dispatchInterval dispatch time in seconds.
 */
-(void) startTimer :(NSNumber *) dispatchInterval;
-(void) dispatch :(NSTimer *)timer;
-(BOOL) isDispatching;
-(void) stop;

# pragma mark - Dispatcher delegator methods.

- (void)didDispatchWithError:(id)sender;
- (void)didDispatchWithSuccess:(id)sender;

@end
