//
//  O2MBatchManager.h
//  O2MC
//
//  Created by Tim Slot on 19/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MDispatcherDelegate.h"
#import "O2MTagger.h"

@interface O2MBatchManager : NSObject <O2MDispatcherDelegate>

@property NSString *endpoint;
@property NSInteger maxRetries;
@property NSString *sessionIdentifier;


-(instancetype) initWithTagger:(O2MTagger*)tagger;

-(void) createBatchWithEvents:(NSMutableArray*)events;
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
