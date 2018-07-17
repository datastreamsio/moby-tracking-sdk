//
// Created by Nicky Romeijn on 23-06-16.
// Copyright (c) 2016 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <Foundation/Foundation.h>
#import "O2MTagger.h"


@interface O2MDispatcher : NSObject {
    NSString* _appName;
    NSURLSession* _session;
    os_log_t _logTopic;
}

@property (assign, nonatomic, readonly) NSInteger connRetries;
@property (assign, nonatomic) NSInteger connRetriesMax;

- (id)init:(NSString*)appName;
-(void) dispatch:(NSString*)endpoint :(NSMutableArray *)funnel;
-(NSMutableDictionary *) getGeneralInfo;
-(void) successHandler;
-(void) errorHandler;
-(NSURLSession *) urlSession;
@end
