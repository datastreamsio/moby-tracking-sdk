//
//  ViewController.m
//  app-obj-c
//
//  Created by Tim Slot on 23/05/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import "ViewController.h"
#import "O2MC.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    // Init
    self._logTopic = os_log_create("io.o2mc.app-obj-c", "testapp-obj-c");

    // Set endpoint textfield with the currently set endpoint.
    [self.endpointTextField setText:[[O2MC sharedInstance] getEndpoint]];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)BtnTouchCreateEvent:(id)sender {
    os_log(self._logTopic, "created event");

    [[O2MC sharedInstance] track:self.eventNameTextField.text];
}

- (IBAction)BtnTouchResetTracking:(id)sender {
    os_log(self._logTopic, "reset tracking");

    [[O2MC sharedInstance] stop];
    [[O2MC sharedInstance] resume];
}

- (IBAction)BtnTouchStopTracking:(id)sender {
    os_log(self._logTopic, "stop tracking");

    [[O2MC sharedInstance] stop];
}

- (IBAction)InputEndpointChanged:(id)sender; {
    os_log(self._logTopic, "endpoint data changed");

    [[O2MC sharedInstance] setEndpoint:self.endpointTextField.text];
}


@end
