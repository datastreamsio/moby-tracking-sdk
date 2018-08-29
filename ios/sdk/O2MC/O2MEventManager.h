//
//  O2MEventManager.h
//  O2MC
//
//  Created by Tim Slot on 18/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Models/O2MEvent.h"

@interface O2MEventManager : NSObject

@property (atomic, readonly, strong) NSMutableArray* events;

-(instancetype) init;

/**
 * Stores an event.
 * @param event event to store
 */
-(void) addEvent :(O2MEvent*)event;
/**
 * Removes all stored events from the device
 */
-(void) clearEvents;
/**
 * Amount of events stored
 */
-(long) eventCount;

@end
