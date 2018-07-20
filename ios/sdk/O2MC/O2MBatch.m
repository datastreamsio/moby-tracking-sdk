//
//  O2MBatch.m
//  datastreams
//
//  Created by Tim Slot on 20/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MBatch.h"


@implementation O2MBatch

-(instancetype) initWithDeviceInformation :(NSDictionary*) deviceInformation; {
    if (self = [super init]) {
        _deviceInformation = deviceInformation;
    }
    return self;
}

@end
