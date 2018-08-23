//
//  O2MEventManager.m
//  datastreams
//
//  Created by Tim Slot on 18/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "O2MEventManager.h"


@implementation O2MEventManager

-(instancetype) init; {
    if (self = [super init]) {
        _events = [[NSMutableArray alloc] init];
    }
    return self;
}

-(void) addEvent :(O2MEvent*)event; {
    [self->_events addObject:event];
}

-(void) clearEvents; {
    [self->_events removeAllObjects];
}

-(long) eventCount; {
    return [self->_events count];
}

@end
