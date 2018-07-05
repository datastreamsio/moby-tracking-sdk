//
//  ViewController.h
//  app-obj-c
//
//  Created by Tim Slot on 23/05/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <UIKit/UIKit.h>
#import "O2MC.h"
#import "O2MTagger.h"

@interface ViewController : UIViewController

@property (strong, nonatomic) O2MC *O2MC;
@property (nonatomic, retain) IBOutlet UITextField *eventNameTextField;
@property (nonatomic, retain) os_log_t _logTopic;

- (IBAction)BtnTouchCreateEvent:(id)sender;
- (IBAction)BtnTouchCreateAlias:(id)sender;
- (IBAction)BtnTouchSetIdentity:(id)sender;
- (IBAction)BtnTouchResetTracking:(id)sender;

@end

