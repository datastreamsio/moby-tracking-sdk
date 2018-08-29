//
//  O2MUtil.m
//  datastreams
//
//  Created by Tim Slot on 17/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import "O2MUtil.h"

@implementation O2MUtil


static NSDateFormatter *o2mDateFormatter = nil;

/**
 * Returns the current time.
 * @return current time formatted in ISO-8601
 */
+(NSString*) currentTimestamp {
    // method found on stackoverflow: https://stackoverflow.com/a/16254918
    // iOS 10+ should use NSISO8601DateFormatter.
    
    if(o2mDateFormatter == nil) {
        o2mDateFormatter = [[NSDateFormatter alloc] init];
        NSLocale *enUSPOSIXLocale = [NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"];
        [o2mDateFormatter setLocale:enUSPOSIXLocale];
        [o2mDateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssZZZZZ"];
    }
    NSDate *now = [NSDate date];
    NSString *iso8601String = [o2mDateFormatter stringFromDate:now];
    
    return iso8601String;
}

+(NSString*) deviceName  {
    // See https://stackoverflow.com/a/11197770/765019
    struct utsname systemInfo;
    uname(&systemInfo);

    return [NSString stringWithCString:systemInfo.machine
                              encoding:NSUTF8StringEncoding];
}

+ (id) objectOrNull:(id)obj; {
    return obj ?: [NSNull null];
}

@end
