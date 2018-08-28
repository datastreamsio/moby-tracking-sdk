//
//  O2MUtil.h
//  O2MC
//
//  Created by Tim Slot on 17/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <sys/utsname.h>

@interface O2MUtil : NSObject
    /**
     * This method generates an ISO 8601 formatted timestamp of the current time.
     */
    +(NSString*) currentTimestamp;

    /**
     * Returns device name (eg. iPhone9,2).
     */
    +(NSString*) deviceName;

/**
* If nil, return NSNull object.
*/
+ (id) objectOrNull:(id)obj;
@end
