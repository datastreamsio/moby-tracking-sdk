//
//  ViewController.m
//  The Food Maze
//
//  Created by Tim Slot on 29/08/2018.
//  Copyright © 2018 o2mc. All rights reserved.
//

#import "ViewController.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    NSLog(@"ViewController:viewDidLoad");
}

- (void) viewWillDisappear:(BOOL)animated; {
    NSLog(@"ViewController:viewWillDisappear");
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
