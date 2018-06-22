//
// Created by Nicky Romeijn on 23-06-16.
// Copyright (c) 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Tagger.h"


@interface Dispatcher : NSObject {
    NSString* _appName;
}

- (id)init:(NSString*)appName;
-(void) dispatch:(NSString*)endpoint :(NSMutableDictionary *)funnel;
-(NSMutableDictionary *) getGeneralInfo;
- (NSString *)getIPAddress;
@end