//
//  O2MTagger.h
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <Foundation/Foundation.h>

@class O2MDispatcher;

@interface O2MTagger : NSObject {
    Boolean _timerHasStarted;
    NSString * _alias;
    NSString * _identity;
    NSMutableDictionary * _funnel;
    NSTimer * _dispatchTimer;
    NSString * _startTime;
    NSString * _timedEvent;
    NSString * _timedEventProperties;
    O2MDispatcher *_dispatcher;
    os_log_t _logTopic;
}

@property NSLock * funnel_lock;
@property NSTimer * dispatchTimer;
@property NSString *endpoint;
@property NSString *appId;
@property NSNumber *dispatchInterval;

-(O2MTagger *) init :(NSString *)appId :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer;
-(void) clearFunnel;
-(void) setEndpoint :(NSString *) endpoint;
-(void) setAppId :(NSString *) appId;
/**
 * Stops tracking of events.
 */
-(void) stop;
-(void) stop :(BOOL) clearFunnel;
/**
 * The time interval between sending events to the backend.
 * @param dispatchInterval time in seconds (defaults to 8)
 */
-(void) setDispatchInterval :(NSNumber *)dispatchInterval;
/**
 * The max amount of connection retries before stopping dispatching.
 * @param maxRetries retry amount (defaults to 5)
 */
-(void) setMaxRetries :(NSInteger)maxRetries;
-(void) addToFunnel :(NSString*)funnelKey :(NSDictionary*)funnelData;

//Tracking methods
/**
 * Tracks an event.
 * Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
 * @param eventName name of tracked event
 */
-(void)track :(NSString*)eventName;
/**
 * Tracks an event with additional data.
 * Essentially adds a new event with the String parameter as name and any properties as JSON String format.
 * @param eventName name of tracked event
 * @param propertiesAsJson anything you'd like to keep track of
 */
-(void)trackWithProperties :(NSString*)eventName :(NSString*)propertiesAsJson;


-(void) dispatch :(NSTimer *)timer;
@end
