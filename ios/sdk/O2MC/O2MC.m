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
    self = [self initWithParams:@"" :[[NSNumber alloc] initWithInt:10]];
    return self;
}

-(instancetype) initWithEndpoint :(NSString *)endpoint;  {
    self = [self initWithParams:endpoint :[[NSNumber alloc] initWithInt:10]];
    return self;
}

 -(instancetype)initWithParams :(NSString *)endpoint :(NSNumber *)dispatchInterval; {
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

-(void) stop :(BOOL) clearFunnel; {
    [self->_tracker stop:clearFunnel];
}

-(void)track :(NSString*)eventName; {
    [self->_tracker track:eventName];
}

-(void)trackWithProperties :(NSString*)eventName :(NSDictionary*)properties; {
    [self->_tracker trackWithProperties:eventName :properties];
}

-(void)setIdentifier :(NSString*) uniqueIdentifier; {
    // TODO: to be implemented
}

-(void)setSessionIdentifier; {
    [self->_tracker setSessionIdentifier];
}

@end

