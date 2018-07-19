//
//  O2MTagger.h
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <Foundation/Foundation.h>
#import "O2MEventManager.h"
#import "O2MEvent.h"

@class O2MDispatcher;

@interface O2MTagger : NSObject {
    NSString * _alias;
    NSString * _identity;
    NSTimer * _dispatchTimer;
    O2MDispatcher *_dispatcher;
    os_log_t _logTopic;
    O2MEventManager* _eventManager;
    dispatch_queue_t _tagQueue;
}

@property NSTimer * dispatchTimer;
@property NSString *endpoint;
@property NSString *appId;
@property NSNumber *dispatchInterval;

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
/**
 * The time interval between sending events to the backend.
 * @param dispatchInterval time in seconds (defaults to 8)
 */
-(void) setDispatchInterval :(NSNumber *)dispatchInterval;

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

#pragma mark - Internal methods

-(void) dispatch :(NSTimer *)timer;
@end
