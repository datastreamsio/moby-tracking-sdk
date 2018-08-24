//
//  ViewController.h
//  app-obj-c
//
//  Created by Tim Slot on 23/05/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <os/log.h>
#import <UIKit/UIKit.h>

@interface ViewController : UIViewController

@property (nonatomic, retain) IBOutlet UITextField *endpointTextField;
@property (nonatomic, retain) IBOutlet UITextField *eventNameTextField;
@property (nonatomic, retain) os_log_t _logTopic;

- (IBAction)BtnTouchCreateEvent:(id)sender;
- (IBAction)BtnTouchResetTracking:(id)sender;
- (IBAction)BtnTouchStopTracking:(id)sender;

- (IBAction)InputEndpointChanged:(id)sender;

@end

