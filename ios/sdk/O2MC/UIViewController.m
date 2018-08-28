//
//  O2MScreenTracker.m
//  datastreams
//
//  Created by Tim Slot on 28/08/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <objc/runtime.h>
#import <UIKit/UIKit.h>
#import "O2MC.h"

@implementation UIViewController (Tracking)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        Class class = [self class];

        SEL originalSelector = @selector(viewWillAppear:);
        SEL swizzledSelector = @selector(O2M_viewDidAppear:);

        Method originalMethod = class_getInstanceMethod(class, originalSelector);
        Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);

        BOOL didAddMethod =
        class_addMethod(class,
                        originalSelector,
                        method_getImplementation(swizzledMethod),
                        method_getTypeEncoding(swizzledMethod));

        if (didAddMethod) {
            class_replaceMethod(class,
                                swizzledSelector,
                                method_getImplementation(originalMethod),
                                method_getTypeEncoding(originalMethod));
        } else {
            method_exchangeImplementations(originalMethod, swizzledMethod);
        }
    });
}

- (void) O2M_viewDidAppear:(BOOL)animated; {
    [self O2M_viewDidAppear:animated];

    // Track viewDidAppear event
    [[O2MC sharedInstance] trackWithProperties:@"viewDidAppear" properties:@{@"title": self.title ?: [NSNull null]}];
}

@end
