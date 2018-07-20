//
//  O2MDispatcherDelegate.h
//  O2MC
//
//  Created by Tim Slot on 20/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

@class O2MDispatcher;

@protocol O2MDispatcherDelegate <NSObject>

-(void) didDispatchWithSuccess:(O2MDispatcher*) sender;
-(void) didDispatchWithError:(O2MDispatcher*) sender;

@end
