//
//  EventManagerTests.m
//  Tests
//
//  Created by Tim Slot on 25/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "O2MEvent.h"
#import "O2MEventManager.h"

@interface EventManagerTests : XCTestCase

@property O2MEventManager* eventManager;

@end

@implementation EventManagerTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
    self->_eventManager = [O2MEventManager sharedManager];
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];

    [self->_eventManager clearEvents];
}

- (void)testClearEvents {
    O2MEvent *event = [[O2MEvent alloc] init:@"foobar"];
    [self->_eventManager addEvent:event];
    
    XCTAssertEqual(self->_eventManager.eventCount, 1);
    [self->_eventManager clearEvents];
    XCTAssertEqual(self->_eventManager.eventCount, 0);
}

- (void)testAddingSingleEvent {
    O2MEvent *event = [[O2MEvent alloc] init:@"foobar2"];
    
    [self->_eventManager addEvent:event];

    XCTAssertEqual(self->_eventManager.eventCount, 1);
    XCTAssertEqualObjects([self->_eventManager.events firstObject], event);
}

- (void)testAddingMultipleEvents {
    long iterations = 1337;
    for(int i=0; i<iterations; i++) {
        O2MEvent *event = [[O2MEvent alloc] init:@"foo"];
        [self->_eventManager addEvent:event];
    }

    XCTAssertEqual([self->_eventManager eventCount], iterations);
}

@end
