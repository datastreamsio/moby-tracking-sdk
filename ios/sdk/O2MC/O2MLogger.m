//
//  O2MLogger.m
//  datastreams
//
//  Created by Tim Slot on 25/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import "O2MLogger.h"

@implementation O2MLogger

-(instancetype) initWithTopic :(char *) topic; {
    if (self = [super init]) {
#ifdef os_log
        self->_logTopic = os_log_create("io.o2mc.sdk", topic);
#endif
        self->_topic = topic;
    }
    return self;
}

-(BOOL) osLogAvailable; {
    // minimal versions for os_log check on runtime.
    if (@available(iOS 10.0, macOS 10.12, tvOS 10.0, *)) {
        return YES;
    }

    return NO;
}

-(void) log :(const char*) logMsg; {
    if([self osLogAvailable]) {
        os_log_debug(self->_logTopic, "%s", logMsg);
    } else {
        NSLog(@"[%s] %s", self->_topic, logMsg);
    }
}

-(void) logI :(const char*) logMsg; {
    if([self osLogAvailable]) {
        os_log_info(self->_logTopic, "%s", logMsg);
    } else {
        NSLog(@"[%s] %s", self->_topic, logMsg);
    }
}

-(void) logD :(const char*) logMsg; {
    if([self osLogAvailable]) {
        os_log_debug(self->_logTopic, "%s", logMsg);
    } else {
        NSLog(@"[%s] %s", self->_topic, logMsg);
    }
}

-(void) logE :(const char*) logMsg; {
    if([self osLogAvailable]) {
        os_log_error(self->_logTopic, "%s", logMsg);
    } else {
        NSLog(@"[%s] %s", self->_topic, logMsg);
    }
}

-(void) logF :(const char*) logMsg; {
    if([self osLogAvailable]) {
        os_log_fault(self->_logTopic, "%s", logMsg);
    } else {
        NSLog(@"[%s] %s", self->_topic, logMsg);
    }
}

@end
