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
-(instancetype) init; {
    self = [self initWithDispatchInterval:[[NSNumber alloc] initWithInt:10] endpoint:@""];
    return self;
}

-(instancetype) initWithEndpoint:(NSString *)endpoint;  {
    self = [self initWithDispatchInterval:[[NSNumber alloc] initWithInt:10] endpoint:endpoint];
    return self;
}

-(instancetype) initWithDispatchInterval:(NSNumber *)dispatchInterval endpoint:(NSString *)endpoint; {
     if (self = [super init]) {
         self->_tracker = [[O2MTagger alloc] init:endpoint :dispatchInterval];

         // Default setting
         [_tracker setMaxRetries:5];
     }
     return self;
}

-(void) setEndpoint :(NSString *) endpoint; {
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

-(void)setIdentifier:(NSString*) uniqueIdentifier; {
    [self->_tracker setIdentifier:uniqueIdentifier];
}

-(void)setSessionIdentifier; {
    [self->_tracker setSessionIdentifier];
}

-(void)track :(NSString*)eventName; {
    [self->_tracker track:eventName];
}

-(void)trackWithProperties:(NSString*)eventName properties:(NSDictionary*)properties; {
    [self->_tracker trackWithProperties:eventName properties:properties];
}

@end

