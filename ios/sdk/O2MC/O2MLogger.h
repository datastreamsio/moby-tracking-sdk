//
//  O2MLogger.h
//  O2MC
//
//  Created by Tim Slot on 25/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <os/log.h>

/**
 * Mock NSLog on production builds.
 */
#ifdef DEBUG
#   define NSLog(...) NSLog(__VA_ARGS__)
#else
#   define NSLog(...) (void)0
#endif


@interface O2MLogger : NSObject {

@private char* _topic;
@private os_log_t _logTopic;

}

#pragma mark - Constructor methods

/**
 * Constructs a logger object with a log topic.
 * @param topic self describing log topic
 */
-(instancetype) initWithTopic :(char*) topic;

#pragma mark - Log methods

/**
 * Sends a default-level log message.
 */
-(void) log :(const char*) logMsg;
/**
 * Sends an info-level log message.
 */
-(void) logI :(const char*) logMsg;
/**
 * Sends a debug-level log message.
 */
-(void) logD :(const char*) logMsg;
/**
 * Sends an error-level log message.
 */
-(void) logE :(const char*) logMsg;
/**
 * Sends a fault-level log message.
 */
-(void) logF :(const char*) logMsg;

@end
