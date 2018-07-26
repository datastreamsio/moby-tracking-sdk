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

typedef enum O2MLogType : NSUInteger {
    ODebug,
    OInfo,
    OError,
    OFault
} O2MLogType;


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
 * @param logMsg log message
 */
-(void) log :(NSString*) logMsg, ... NS_FORMAT_FUNCTION(1,2);
/**
 * Sends an info-level log message.
 * @param logMsg log message
 */
-(void) logI :(NSString*) logMsg, ... NS_FORMAT_FUNCTION(1,2);
/**
 * Sends a debug-level log message.
 * @param logMsg log message
 */
-(void) logD :(NSString*) logMsg, ... NS_FORMAT_FUNCTION(1,2);
/**
 * Sends an error-level log message.
 * @param logMsg log message
 */
-(void) logE :(NSString*) logMsg, ... NS_FORMAT_FUNCTION(1,2);
/**
 * Sends a fault-level log message.
 * @param logMsg log message
 */
-(void) logF :(NSString*) logMsg, ... NS_FORMAT_FUNCTION(1,2);

@end
