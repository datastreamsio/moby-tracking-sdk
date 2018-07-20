//
//  O2MBatch.h
//  O2MC
//
//  Created by Tim Slot on 20/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface  O2MBatch : NSObject

@property (readonly) NSDictionary *deviceInformation;
@property NSArray *events;
@property long retries;
@property NSString *timestamp;

-(instancetype) initWithDeviceInformation :(NSDictionary*) deviceInformation;

@end
