//
//  O2MConfig.h
//  O2MC
//
//  Created by Tim Slot on 04/09/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//
#import <Foundation/Foundation.h>

@interface O2MConfig : NSObject

@property (class, nonatomic, assign, readonly) NSNumber *batchInterval;
@property (class, nonatomic, assign, readonly) NSNumber *dispatchInterval;
@property (class, nonatomic, assign, readonly) NSString *httpEndpoint;
@property (class, nonatomic, assign, readonly) NSInteger maxRetries;

@end
