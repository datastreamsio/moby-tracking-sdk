//
//  O2MC.m
//  O2MC
//
//  Created by Nicky Romeijn on 09-08-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MC.h"
#import "O2MTagger.h"

@implementation O2MC

+(nonnull instancetype) sharedInstance; {
    static O2MC *sharedInstance = nil;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        sharedInstance = [[O2MC alloc] init];
    });

    return sharedInstance;
}

/**
 * Constructs the tracking SDK. NOTE: be sure to set an endpoint.
 * @return an O2MC instance
 */
-(nonnull instancetype) init; {
    self = [self initWithDispatchInterval:[[NSNumber alloc] initWithInt:10] endpoint:@""];
    return self;
}

/**
 * Constructs the tracking SDK.
 * @param dispatchInterval time in seconds between dispatches
 * @param endpoint http(s) URL which should be publicly reachable
 * @return an O2MC instance
 */
-(nonnull instancetype) initWithDispatchInterval:(nonnull NSNumber *)dispatchInterval endpoint:(nonnull NSString *)endpoint; {
     if (self = [super init]) {
         self->_tracker = [[O2MTagger alloc] init:endpoint :dispatchInterval];

         // Default setting
         [_tracker setMaxRetries:5];
     }
     return self;
}

-(nonnull NSString*) getEndpoint; {
    return [self->_tracker getEndpoint];
}

-(void) setEndpoint:(nonnull NSString *)endpoint; {
    [self->_tracker setEndpoint:endpoint];
}

-(void) setMaxRetries :(NSInteger)maxRetries; {
    [self->_tracker setMaxRetries:maxRetries];
}

-(void) stop; {
    [self->_tracker stop];
}

-(void) stop:(BOOL)clearFunnel; {
    [self->_tracker stop:clearFunnel];
}

-(void) resume; {
    [self->_tracker resume];
}

-(void)setIdentifier:(nullable NSString*) uniqueIdentifier; {
    [self->_tracker setIdentifier:uniqueIdentifier];
}

-(void)setSessionIdentifier; {
    [self->_tracker setSessionIdentifier];
}

-(void)track:(nonnull NSString*)eventName; {
    [self->_tracker track:eventName];
}

-(void)trackWithProperties:(nonnull NSString*)eventName properties:(nonnull NSDictionary*)properties; {
    [self->_tracker trackWithProperties:eventName properties:properties];
}

@end

