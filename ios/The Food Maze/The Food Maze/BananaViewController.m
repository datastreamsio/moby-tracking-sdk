//
//  BananaViewController.m
//  The Food Maze
//
//  Created by Tim Slot on 29/08/2018.
//  Copyright © 2018 o2mc. All rights reserved.
//

#import "BananaViewController.h"

@interface BananaViewController ()

@end

@implementation BananaViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSLog(@"BananaViewController:viewDidLoad");
}

- (void)viewWillDisappear:(BOOL)animated {
    NSLog(@"BananaViewController:viewWillDisappear");
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
