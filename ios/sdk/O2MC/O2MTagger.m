//
//  O2MTagger.m
//  O2MTagger
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import "O2MTagger.h"
#import "O2MDispatcher.h"

@implementation O2MTagger

static int objectCount = 0;

-(O2MTagger *) init :(NSString *)appId :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer; {
    self = [super init];
    
    _funnel = [[NSMutableDictionary alloc] init];
    self.funnel_lock = [[NSLock alloc] init];
    _dispatcher = [[O2MDispatcher alloc] init :appId];
    _alias = [[NSUUID UUID] UUIDString];
    _identity = @"";
    _logTopic = os_log_create("io.o2mc.sdk", "tagger");
    
    [self setAppId:appId];
    [self setEndpoint:endpoint];
    [self setDispatchInterval:dispatchInterval];
    NSDictionary *buildInitFunnel = @{
                                      @"alias" : _alias,
                                      @"identity": _identity
                                      };
    [self addToFunnel:@"alias" : buildInitFunnel];
    if(objectCount == 1 || forceStartTimer){
        _dispatchTimer = [NSTimer timerWithTimeInterval:dispatchInterval.floatValue
                                                 target:self
                                               selector:@selector(dispatch:)
                                               userInfo:nil
                                                repeats:YES];
        [[NSRunLoop mainRunLoop] addTimer:_dispatchTimer forMode:NSRunLoopCommonModes];
        os_log(self->_logTopic, "Init tagger with timer");
    } else {
        os_log(self->_logTopic, "Init tagger");
    }
    objectCount++;
    return self;
}

#pragma mark - Configuration methods

-(void) setMaxRetries :(NSInteger)maxRetries; {
    if (_dispatcher) {
        [_dispatcher setConnRetriesMax: maxRetries];
    }
}

#pragma mark - Internal methods

-(void) dispatch:(NSTimer *)timer;{
    [self.funnel_lock lock];
    if(_funnel.count > 0){
        if(_dispatcher.connRetries < _dispatcher.connRetriesMax) {
            #ifdef DEBUG
                os_log_debug(self->_logTopic, "Dispatcher has been triggered");
            #endif
            [_dispatcher dispatch :_endpoint :_funnel];
        } else {
            os_log_info(self->_logTopic, "Reached max connection retries (%ld), stopping dispatcher.", (long)_dispatcher.connRetriesMax);

            // Stopping the time based interval loop.
            [timer invalidate];
        }
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

    #ifdef DEBUG
        os_log_debug(self->_logTopic, "Array Count = %lu && number of items %u", (unsigned long)[_funnel count], numberOfItems);
    #endif
    [self.funnel_lock unlock];
    
}

#pragma mark - Tracking methods

-(void)track :(NSString*)eventName; {
    if (![_dispatchTimer isValid]) return;
    #ifdef DEBUG
        os_log_debug(self->_logTopic, "Track %@", eventName);
    #endif

    NSDictionary *funnel = @{
                             @"event" : eventName,
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[self getIsoTimestamp]
                             };
    
    [self addToFunnel:eventName :funnel];
}

-(void)trackWithProperties:(NSString*)eventName :(NSString*)propertiesAsJson;
{
    if (![_dispatchTimer isValid]) return;
    #ifdef DEBUG
        os_log_debug(self->_logTopic, "Track %@:%@", eventName, propertiesAsJson);
    #endif

    NSDictionary *funnel = @{
                             @"event" : eventName,
                             @"alias":_alias,
                             @"identitiy":_identity,
                             @"time":[self getIsoTimestamp],
                             @"properties":propertiesAsJson
                             };
    [self addToFunnel:eventName :funnel];
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


-(void)stop {
    [self stop:YES];
}

-(void)stop:(BOOL) clearFunnel; {
    os_log_info(self->_logTopic, "stopping tracking");
    [_dispatchTimer invalidate];

    if (clearFunnel == YES) {
        #ifdef DEBUG
            os_log_debug(self->_logTopic, "clearing the funnel");
        #endif
        [self clearFunnel];
    }
}

@end



