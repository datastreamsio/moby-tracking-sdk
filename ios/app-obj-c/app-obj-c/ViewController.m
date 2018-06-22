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

    self.O2MC = [[O2MC alloc] init:@"app-obj-c" : @"http://127.0.0.1:3000/events" :  [NSNumber numberWithInt:10] :YES];
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)BtnTouchCreateEvent:(id)sender {
    NSLog(@"created event");

    [self.O2MC.tracker track:self.eventNameTextField.text];
}

- (IBAction)BtnTouchCreateAlias:(id)sender {
    NSLog(@"created alias");

    [self.O2MC.tracker createAlias:self.eventNameTextField.text];
}

- (IBAction)BtnTouchSetIdentity:(id)sender {
    NSLog(@"set identity");

    [self.O2MC.tracker identify:self.eventNameTextField.text];
}

- (IBAction)BtnTouchResetTracking:(id)sender {
    NSLog(@"reset tracking");

    [self.O2MC.tracker clearFunnel];
}


@end
