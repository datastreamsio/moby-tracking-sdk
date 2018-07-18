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
 -(id)init :(NSString *)endpoint :(NSNumber *)dispatchInterval; {
     self->_tracker =  [[O2MTagger alloc] init:endpoint :dispatchInterval];
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

-(void)trackWithProperties :(NSString*)eventName :(NSString*)propertiesAsJson; {
    [self->_tracker trackWithProperties:eventName :propertiesAsJson];
}

@end

