//
//  O2MUtil.m
//  datastreams
//
//  Created by Tim Slot on 17/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import "O2MUtil.h"

@implementation O2MUtil

+(NSString*) currentTimestamp {
    // method found on stackoverflow: https://stackoverflow.com/a/16254918
    // iOS 10+ should use NSISO8601DateFormatter.
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    NSLocale *enUSPOSIXLocale = [NSLocale localeWithLocaleIdentifier:@"en_US_POSIX"];
    [dateFormatter setLocale:enUSPOSIXLocale];
    [dateFormatter setDateFormat:@"yyyy-MM-dd'T'HH:mm:ssZZZZZ"];
    
    NSDate *now = [NSDate date];
    NSString *iso8601String = [dateFormatter stringFromDate:now];
    
    return iso8601String;
}

+(NSString*) deviceName  {
    // See https://stackoverflow.com/a/11197770/765019
    struct utsname systemInfo;
    uname(&systemInfo);

    return [NSString stringWithCString:systemInfo.machine
                              encoding:NSUTF8StringEncoding];
}

@end
