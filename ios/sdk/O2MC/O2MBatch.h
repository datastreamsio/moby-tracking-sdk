//
//  O2MBatch.h
//  O2MC
//
//  Created by Tim Slot on 20/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MEvent.h"
#import "O2MUtil.h"

@interface  O2MBatch : NSObject

@property (readonly) NSDictionary *deviceInformation;
@property NSMutableArray *events;
@property long number;
@property long retries;
@property NSString *timestamp;

-(instancetype) initWithParams :(NSDictionary*) deviceInformation :(int) number;

-(void) addEvent :(O2MEvent*) event;
-(void) addRetry;
-(NSArray*) eventsAsString;

@end
