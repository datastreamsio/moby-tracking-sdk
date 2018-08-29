//
//  AppleViewController.m
//  The Food Maze
//
//  Created by Tim Slot on 29/08/2018.
//  Copyright © 2018 o2mc. All rights reserved.
//

#import "AppleViewController.h"

@interface AppleViewController ()

@end

@implementation AppleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSLog(@"AppleViewController:viewDidLoad");
}

- (void)viewWillDisappear:(BOOL)animated {
    NSLog(@"AppleViewController:viewWillDisappear");
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
