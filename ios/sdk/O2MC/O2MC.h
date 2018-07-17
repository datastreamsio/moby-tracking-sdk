//
//  O2MC.h
//  O2MC
//
//  Created by Nicky Romeijn on 16-06-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>

@class O2MTagger;

@interface O2MC : NSObject {
}

@property (readonly, nonatomic) O2MTagger *tracker;

-(id)init :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer;

@end
