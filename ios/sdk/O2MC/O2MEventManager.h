//
//  O2MEventManager.h
//  O2MC
//
//  Created by Tim Slot on 18/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface O2MEventManager : NSObject

@property (nonatomic, strong) NSMutableArray* events;
@property (nonatomic, strong) dispatch_queue_t eventQueue;

+ (instancetype)sharedManager;
-(void) addEvent :(NSDictionary*)event;
-(void) clearEvents;

@end
