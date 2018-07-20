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
    self.O2MC = [[O2MC alloc] initWithEndpoint:@"http://127.0.0.1:5000/events"];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)BtnTouchCreateEvent:(id)sender {
    os_log(self._logTopic, "created event");

    [self.O2MC track:self.eventNameTextField.text];
}

- (IBAction)BtnTouchResetTracking:(id)sender {
    os_log(self._logTopic, "reset tracking");

    [self.O2MC.tracker clearFunnel];
}

- (IBAction)BtnTouchStopTracking:(id)sender {
    os_log(self._logTopic, "stop tracking");

    [self.O2MC stop];
}


@end
