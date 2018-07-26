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
        if([self osLogAvailable]) {
            self->_logTopic = os_log_create("io.o2mc.sdk", topic);
        }
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

-(const char*) strFmtToChar :(NSString*)fmt :(va_list)args; {
    // create a NSString from all the arguments and return it as a char array.
    return [[[NSString alloc] initWithFormat:fmt arguments:args] UTF8String];
}

-(void) actualLogger :(O2MLogType) logType :(const char *) logMsg; {
    if([self osLogAvailable]) {
        switch(logType) {
            case ODebug:
                os_log_debug(self->_logTopic, "%s", logMsg);
                break;
            case OInfo:
                os_log_info(self->_logTopic, "%s", logMsg);
                break;
            case OError:
                os_log_error(self->_logTopic, "%s", logMsg);
                break;
            case OFault:
                os_log_fault(self->_logTopic, "%s", logMsg);
                break;
            default:
                break;
        }
    } else {
        NSLog(@"[%s] %s", self->_topic, logMsg);
    }
}

-(void) log :(NSString*) logMsg, ... ; {
    va_list args;
    va_start(args, logMsg);
    [self actualLogger:ODebug :[self strFmtToChar:logMsg :args]];
    va_end(args);
}

-(void) logI :(NSString*) logMsg, ... ; {
    va_list args;
    va_start(args, logMsg);
    [self actualLogger:OInfo :[self strFmtToChar:logMsg :args]];
    va_end(args);
}

-(void) logD :(NSString*) logMsg, ... ; {
    va_list args;
    va_start(args, logMsg);
    [self actualLogger:ODebug :[self strFmtToChar:logMsg :args]];
    va_end(args);
}

-(void) logE :(NSString*) logMsg, ... ; {
    va_list args;
    va_start(args, logMsg);
    [self actualLogger:OError :[self strFmtToChar:logMsg :args]];
    va_end(args);
}

-(void) logF :(NSString*) logMsg, ... ; {
    va_list args;
    va_start(args, logMsg);
    [self actualLogger:OFault :[self strFmtToChar:logMsg :args]];
    va_end(args);
}

@end
