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

-(void) log :(const char*) logMsg; {
#ifdef os_log
    os_log_debug(self->_logTopic, "%s", logMsg);
#else
    NSLog(@"[%s] %s", self->_topic, logMsg);
#endif
}

-(void) logI :(const char*) logMsg; {
#ifdef os_log
    os_log_info(self->_logTopic, "%s", logMsg);
#else
    NSLog(@"[%s] %s", self->_topic, logMsg);
#endif
}

-(void) logD :(const char*) logMsg; {
#ifdef os_log
    os_log_debug(self->_logTopic, "%s", logMsg);
#else
    NSLog(@"[%s] %s", self->_topic, logMsg);
#endif
}

-(void) logE :(const char*) logMsg; {
#ifdef os_log
    os_log_error(self->_logTopic, "%s", logMsg);
#else
    NSLog(@"[%s] %s", self->_topic, logMsg);
#endif
}

-(void) logF :(const char*) logMsg; {
#ifdef os_log
    os_log_fault(self->_logTopic, "%s", logMsg);
#else
    NSLog(@"[%s] %s", self->_topic, logMsg);
#endif
}

@end
