//
//  O2MTagger.h
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MBatchManager.h"
#import "O2MDispatcher.h"
#import "O2MEventManager.h"
#import "O2MEvent.h"
#import "O2MLogger.h"
#import "O2MUtil.h"


@interface O2MTagger : NSObject {
    O2MBatchManager *_batchManager;
    O2MEventManager* _eventManager;
    O2MLogger *_logger;
    dispatch_queue_t _tagQueue;
}

@property NSTimer * dispatchTimer;
@property (nonatomic) NSString *endpoint;
@property (copy) NSString *appId;

-(O2MTagger *) init :(NSString *)endpoint :(NSNumber *)dispatchInterval;
#pragma mark - Configuration methods

/**
 * Configures an end point where the events will be dispatched to.
 * @param endpoint http(s) URL which should be publicly reachable
 */
-(void) setEndpoint :(NSString *) endpoint;
/**
 * The max amount of connection retries before stopping dispatching.
 * @param maxRetries retry amount (defaults to 5)
 */
-(void) setMaxRetries :(NSInteger)maxRetries;

#pragma mark - Control methods

/**
 * Removes current events which are not yet dispatched to the backend.
 */
-(void) clearFunnel;

/**
 * Stops tracking of events.
 */
-(void) stop;
/**
 * Stops tracking of events.
 * @param clearFunnel clears any existing events
 */
-(void) stop :(BOOL) clearFunnel;

#pragma mark - Tracking methods

/**
 * Tracks an event.
 * Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
 * @param eventName name of tracked event
 */
-(void)track :(NSString*)eventName;
/**
 * Tracks an event with additional data.
 * Essentially adds a new event with the String parameter as name and any additonal properties.
 * @param eventName name of tracked event
 * @param properties anything you'd like to keep track of
 */
-(void)trackWithProperties :(NSString*)eventName :(NSDictionary*)properties;

@end
