//
//  O2MC.m
//  O2MC
//
//  Created by Nicky Romeijn on 09-08-16.
//  Copyright © 2016 Adversitement. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "O2MC.h"
#import "O2Tagger.h"

@implementation O2MC
 -(id)init :(NSString *)appId :(NSString *)endpoint :(NSNumber *)dispatchInterval :(Boolean) forceStartTimer; {
     self->_tracker =  [[O2Tagger alloc] init:appId :endpoint :dispatchInterval :forceStartTimer];
    return self;
}
@end

