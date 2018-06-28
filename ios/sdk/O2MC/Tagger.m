//
//  Datastreams.m
//  Datastreams
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import "Tagger.h"
#import "Dispatcher.h"

@implementation Tagger

static int objectCount = 0;

-(Tagger *) init :(NSString *)appId :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer; {
    self = [super init];
    
    _funnel = [[NSMutableDictionary alloc] init];
    self.funnel_lock = [[NSLock alloc] init];
    _dispatcher = [[Dispatcher alloc] init :appId];
    _alias = [[NSUUID UUID] UUIDString];
    _identity = @"";
    
    [self setAppId:appId];
    [self setEndpoint:endpoint];
    [self setDispatchInterval:dispatchInterval];
    NSDictionary *buildInitFunnel = @{
                                      @"alias" : _alias,
                                      @"identity": _identity
                                      };
    [self addToFunnel:@"alias" : buildInitFunnel];
    if(objectCount == 1 || forceStartTimer){
        NSTimer *timer = [NSTimer timerWithTimeInterval:[dispatchInterval floatValue]
                                                 target:self
                                               selector:@selector(dispatch:)
                                               userInfo:nil
                                                repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:timer forMode:NSRunLoopCommonModes];
        NSLog(@"Init tagger with timer");
    } else {
        NSLog(@"Init tagger");
    }
    objectCount++;
    return self;
}

-(void) dispatch:(NSTimer *)timer;{
    [self.funnel_lock lock];
    if([_funnel count] > 0){
        NSLog(@"Dispatcher has been triggered");
        [_dispatcher dispatch :_endpoint :_funnel];
    }
    [self.funnel_lock unlock];
}

-(void) clearFunnel; {
    [self.funnel_lock lock];
    [_funnel removeAllObjects];
    [self.funnel_lock unlock];
}

-(void) addToFunnel :(NSString*)funnelKey :(NSDictionary*)funnelData; {
    [self.funnel_lock lock];
    if([_funnel objectForKey:funnelKey] == nil){
        [_funnel setObject :[[NSMutableArray alloc] init] forKey :funnelKey];
    }
    [[_funnel objectForKey:funnelKey] addObject:funnelData];
    
    
    int numberOfItems = 0;
    for(id key in _funnel){
        numberOfItems += [[_funnel objectForKey:key] count];
    }
    //        if(numberOfItems > 1){
    //            NSLog(@"I am bigger than 1 and am starting to dispatch");
    //            [_dispatcher dispatch :_endpoint :_funnel];
    //        }
    NSLog(@"Array Count = %u && number of items %u", [_funnel count], numberOfItems);
    [self.funnel_lock unlock];
    
}

-(void)track :(NSString*)eventName; {
    NSLog(@"Track %@", eventName);
    NSDictionary *funnel = @{
                             @"event" : eventName,
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[self getIsoTimestamp]
                             };
    
    [self addToFunnel:eventName :funnel];
}

-(void)trackWithProperties:(NSString*)eventName:(NSString*)propertiesAsJson;
{
    NSLog(@"Track %@:%@", eventName, propertiesAsJson);
    NSDictionary *funnel = @{
                             @"event" : eventName,
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[self getIsoTimestamp],
                             @"properties":propertiesAsJson
                             };
    [self addToFunnel:eventName :funnel];
}


-(void)createAlias:(NSString*)alias; {
    NSLog(@"Alias %@", alias);
    _alias = alias;
    NSDictionary *funnel = @{
                             @"event" : @"alias",
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[self getIsoTimestamp]
                             };
    [self addToFunnel:@"alias" :funnel];
}

-(void)identify:(NSString *)identity; {
    NSLog(@"Identity %@", identity);
    _identity = identity;
    NSDictionary *funnel = @{
                             @"event" : @"identity",
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[self getIsoTimestamp]
                             };
    [self addToFunnel:@"identity" :funnel];
}

-(NSString*) getIsoTimestamp {
    // method found on stackoverflow: https://stackoverflow.com/a/16254918
    // iOS 10+ should use NSISO8601DateFormatter.

    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    NSLocale *enUSPOSIXLocale = [NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"];
    [dateFormatter setLocale:enUSPOSIXLocale];
    [dateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssZZZZZ"];

    NSDate *now = [NSDate date];
    NSString *iso8601String = [dateFormatter stringFromDate:now];

    return iso8601String;
}

-(void)timeEventStartWithProperties:(NSString*)eventName:(NSString*)propertiesAsJson;{
    NSLog(@"timeEventStartWithProperties %@:%@", eventName, propertiesAsJson);
    _startTime = [self getIsoTimestamp];
    _timedEvent = eventName;
    _timedEventProperties = propertiesAsJson;
}

-(void)timeEventStart:(NSString*)eventName;{
    NSLog(@"timeEventStart %@", eventName);
    _startTime = [self getIsoTimestamp];
    _timedEvent = eventName;
}


-(void)timeEventStop:(NSString*)eventName;{
    NSLog(@"timeEventStop %@", eventName);
    if([_timedEvent isEqualToString:eventName]){
        if(_timedEventProperties){
            NSDictionary *funnel = @{
                                     @"event" : eventName,
                                     @"alias":_alias,
                                     @"identitiy":_identity,
                                     @"timeStart":_startTime,
                                     @"timeStop":[self getIsoTimestamp],
                                     @"properties":_timedEventProperties
                                     };
            [self addToFunnel:eventName :funnel];
        } else {
            NSDictionary *funnel = @{
                                     @"event" : eventName,
                                     @"alias":_alias,
                                     @"identitiy":_identity,
                                     @"timeStart":_startTime,
                                     @"timeStop":[self getIsoTimestamp]
                                     };
            [self addToFunnel:eventName :funnel];
        }
    }
}

@end



