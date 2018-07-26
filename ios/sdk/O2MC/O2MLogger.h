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

+(void) log;
+(void) logI;
+(void) logD;
+(void) logE;
+(void) logF;

@end
