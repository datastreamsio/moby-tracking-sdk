//
//  O2MBatchManager.h
//  O2MC
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MDispatcherDelegate.h"

@interface O2MBatchManager : NSObject <O2MDispatcherDelegate>

@property (nonatomic, readonly) int batchNumber;
@property NSString *endpoint;
@property NSInteger maxRetries;
@property (nonatomic, readonly) NSInteger connRetries;
@property NSString *sessionIdentifier;


-(instancetype) init;

-(void) createBatchWithEvents:(NSArray*)events;
/**
 * Starts dispatching with a specific dispatch interval.
 * @param dispatchInterval dispatch time in seconds.
 */
-(void) dispatchWithInterval :(NSNumber *) dispatchInterval;
-(BOOL) isDispatching;
-(void) stop;

# pragma mark - Dispatcher delegator methods.

- (void)didDispatchWithError:(id)sender;
- (void)didDispatchWithSuccess:(id)sender;

@end
