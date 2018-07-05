//
// Created by Nicky Romeijn on 23-06-16.
// Copyright (c) 2016 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <Foundation/Foundation.h>
#import "O2Tagger.h"


@interface O2Dispatcher : NSObject {
    NSString* _appName;
    NSURLSession* _session;
    os_log_t _logTopic;
}

- (id)init:(NSString*)appName;
-(void) dispatch:(NSString*)endpoint :(NSMutableDictionary *)funnel;
-(NSMutableDictionary *) getGeneralInfo;
-(NSURLSession *) urlSession;
@end
