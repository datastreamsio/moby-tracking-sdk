//
//  O2Tagger.h
//  O2Tagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <Foundation/Foundation.h>

@class O2Dispatcher;

@interface O2Tagger : NSObject {
    Boolean _timerHasStarted;
    NSString * _alias;
    NSString * _identity;
    NSMutableDictionary * _funnel;
    //    NSTimer * _dispatchTimer;
    NSString * _startTime;
    NSString * _timedEvent;
    NSString * _timedEventProperties;
    O2Dispatcher *_dispatcher;
    os_log_t _logTopic;
}

@property NSLock * funnel_lock;
@property NSTimer * dispatchTimer;
@property NSString *endpoint;
@property NSString *appId;
@property NSNumber *dispatchInterval;

-(O2Tagger *) init :(NSString *)appId :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer;
-(void) clearFunnel;
-(void) setEndpoint :(NSString *) endpoint;
-(void) setAppId :(NSString *) appId;
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
