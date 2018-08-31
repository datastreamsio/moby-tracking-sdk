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
#import "O2MUtil.h"

@implementation UIViewController (Tracking)

+ (void)load {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        BOOL trackScreen = [[[NSBundle mainBundle] objectForInfoDictionaryKey:@"O2M_TRACK_VIEWS"] boolValue];

        // Only swizzle when explicitly requested
        if (!trackScreen)
            return;

        [self swizzleMethodBySelector:@selector(viewWillAppear:) swizzledSelector:@selector(O2M_viewDidAppear:)];
        [self swizzleMethodBySelector:@selector(viewWillDisappear:) swizzledSelector:@selector(O2M_viewWillDisappear:)];
    });
}

+ (void) swizzleMethodBySelector:(SEL)originalSelector swizzledSelector:(SEL)swizzledSelector; {
    Class class = [self class];

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
}

- (void) O2M_viewDidAppear:(BOOL)animated; {
    [self O2M_viewDidAppear:animated];

    // Track viewDidAppear event
    [[O2MC sharedInstance] trackWithProperties:@"viewStart" properties:@{@"name": NSStringFromClass([self class])}];
}

- (void)O2M_viewWillDisappear:(BOOL)animated; {
    [self O2M_viewWillDisappear:(BOOL)animated];

    // Track viewDidDisappear event
    [[O2MC sharedInstance] trackWithProperties:@"viewStop" properties:@{@"name": NSStringFromClass([self class])}];
}

@end
