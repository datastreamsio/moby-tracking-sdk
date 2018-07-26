//
//  O2MLogger.h
//  O2MC
//
//  Created by Tim Slot on 25/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import <Foundation/Foundation.h>

/**
 * Mock NSLog on production builds.
 */
#ifdef DEBUG
#   define NSLog(...) NSLog(__VA_ARGS__)
#else
#   define NSLog(...) (void)0
#endif


@interface O2MLogger : NSObject

/**
 * Sends a default-level log message.
 */
-(void) log :(NSString*) logMsg;
/**
 * Sends an info-level log message.
 */
-(void) logI :(NSString*) logMsg;
/**
 * Sends a debug-level log message.
 */
-(void) logD :(NSString*) logMsg;
/**
 * Sends an error-level log message.
 */
-(void) logE :(NSString*) logMsg;
/**
 * Sends a fault-level log message.
 */
-(void) logF :(NSString*) logMsg;

@end
