//
//  Datastreams.h
//  Datastreams
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>

@class Dispatcher;

@interface Tagger : NSObject {
    Boolean _timerHasStarted;
    NSString * _alias;
    NSString * _identity;
    NSMutableDictionary * _funnel;
    //    NSTimer * _dispatchTimer;
    NSString * _startTime;
    NSString * _timedEvent;
    NSString * _timedEventProperties;
    Dispatcher *_dispatcher;
}

+(int) objectCount;
@property NSLock * funnel_lock;
@property NSTimer * dispatchTimer;
@property NSString *endpoint;
@property NSString *appId;
@property NSNumber *dispatchInterval;

-(Tagger *) init :(NSString *)appId :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer;
-(void) clearFunnel;
-(void) setEndpoint :(NSString *) endpoint;
-(void) setAppid :(NSString *) appId;
-(void) setDispatchInterval :(NSNumber *)dispatchInterval;
-(void) addToFunnel :(NSString*)funnelKey :(NSDictionary*)funnelData;

//Tracking methods
-(void)track :(NSString*)eventName;
-(void)trackWithProperties :(NSString*)eventName :(NSString*)propertiesAsJson;
-(void)createAlias :(NSString*)alias;
-(void)identify :(NSString *)identity;
-(void)timeEventStartWithProperties :(NSString*)eventName :(NSString*)propertiesAsJson;
-(void)timeEventStart:(NSString*)eventName;
-(void)timeEventStop:(NSString*)eventName;


-(void) dispatch :(NSTimer *)timer;
-(NSString*) getIsoTimestamp;
@end
