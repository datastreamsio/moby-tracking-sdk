//
//  O2MEventManager.h
//  O2MC
//
//  Created by Tim Slot on 18/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MEvent.h"

@interface O2MEventManager : NSObject

@property (readonly, strong) NSMutableArray* events;
@property (nonatomic, readonly, strong) dispatch_queue_t eventQueue;

+ (instancetype)sharedManager;
/**
 * Stores an event.
 * @param event event to store
 */
-(void) addEvent :(O2MEvent*)event;
/**
 * Removes all stored events from the device
 */
-(void) clearEvents;

@end
