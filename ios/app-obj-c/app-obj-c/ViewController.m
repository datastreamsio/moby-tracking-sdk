//
//  ViewController.m
//  app-obj-c
//
//  Created by Tim Slot on 23/05/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self._logTopic = os_log_create("io.o2mc.app-obj-c", "testapp-obj-c");
    self.O2MC = [[O2MC alloc] init:@"app-obj-c" : @"http://127.0.0.1:5000/events" :  [NSNumber numberWithInt:10] :YES];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)BtnTouchCreateEvent:(id)sender {
    os_log(self._logTopic, "created event");

    [self.O2MC.tracker track:self.eventNameTextField.text];
}

- (IBAction)BtnTouchCreateAlias:(id)sender {
    os_log(self._logTopic, "created alias");

    [self.O2MC.tracker createAlias:self.eventNameTextField.text];
}

- (IBAction)BtnTouchSetIdentity:(id)sender {
    os_log(self._logTopic, "set identity");

    [self.O2MC.tracker identify:self.eventNameTextField.text];
}

- (IBAction)BtnTouchResetTracking:(id)sender {
    os_log(self._logTopic, "reset tracking");

    [self.O2MC.tracker clearFunnel];
}

- (IBAction)BtnTouchStopTracking:(id)sender {
    os_log(self._logTopic, "stop tracking");

    [self.O2MC.tracker stop];
}


@end
