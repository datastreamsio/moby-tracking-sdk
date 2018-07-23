//
//  O2MBatch.m
//  datastreams
//
//  Created by Tim Slot on 20/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MBatch.h"


@implementation O2MBatch

-(instancetype) initWithBatchNumber:(int)number; {
    if (self = [super init]) {
        _events = [[NSMutableArray alloc] init];
        _number = number;
        _timestamp = [O2MUtil currentTimestamp];
    }
    return self;
}

-(instancetype) initWithDeviceInformation :(NSDictionary*) deviceInformation; {
    if (self = [super init]) {
        _deviceInformation = deviceInformation;
        _timestamp = [O2MUtil currentTimestamp];
    }
    return self;
}

-(void) addEvent :(O2MEvent*) event; {
    [_events addObject:event];
}

-(void) addRetry; {
    self.retries++;
}

-(NSArray*) eventsAsString; {
    NSMutableArray *mArray = [[NSMutableArray alloc] init];

    int i;
    for(i=0; i<_events.count; i++) {
        [mArray addObject:[_events[i] toDict]];
    }

    return mArray;
}

@end
