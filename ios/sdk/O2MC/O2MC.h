//
//  O2MC.h
//  O2MC
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>

@class O2MTagger;

@interface O2MC : NSObject {
}

@property (readonly, nonatomic) O2MTagger *tracker;


#pragma mark - Constructors

/**
 * Constructs the tracking SDK. NOTE: be sure to set an endpoint.
 */
-(instancetype) init;
/**
 * Constructs the tracking SDK.
 * @param endpoint http(s) URL which should be publicly reachable
 */
-(instancetype) initWithEndpoint :(NSString *)endpoint;
/**
 * Constructs the tracking SDK.
 * @param endpoint http(s) URL which should be publicly reachable
 * @param dispatchInterval time in seconds between dispatches
 */
-(instancetype) initWithParams :(NSString *)endpoint :(NSNumber *)dispatchInterval;

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

/**
 * Sets an identifier for a user. This identifier will be sent along the tracked events.
 * This could be useful to correlate various batches with each other.
 *
 * @param uniqueIdentifier unique string which identifies a user.
 */
-(void)setIdentifier :(NSString*) uniqueIdentifier;

/**
 * Sets a random session identifier to identify a user.
 * The identifier is based on a UUID.
 */
-(void)setSessionIdentifier;

@end
