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
-(nonnull instancetype) init; {
    self = [self initWithDispatchInterval:[[NSNumber alloc] initWithInt:10] endpoint:@""];
    return self;
}

-(nonnull instancetype) initWithEndpoint:(nonnull NSString *)endpoint;  {
    self = [self initWithDispatchInterval:[[NSNumber alloc] initWithInt:10] endpoint:endpoint];
    return self;
}

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

