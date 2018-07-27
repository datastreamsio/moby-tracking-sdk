//
//  O2MEventTests.m
//  Tests
//
//  Created by Tim Slot on 27/07/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "O2MEvent.h"

@interface O2MEventTests : XCTestCase

@end

@implementation O2MEventTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

- (void)testInitEvent {
    NSString *eventName = @"testEvent";
    O2MEvent *event = [[O2MEvent alloc] init:eventName];

    // Test if we have an event name set.
    XCTAssertNotNil(event.event);
    // Test if the set event name equals the specified event name.
    XCTAssertEqual(event.event, eventName);
    // Test if a timestamp has been set.
    XCTAssertNotNil(event.timestamp);
}

- (void)testInitEventWithProps {
    NSString *eventName = @"testEvent";
    NSDictionary *eventProps = @{
                                @"foo": @"bar"
                                };
    O2MEvent *event = [[O2MEvent alloc] initWithProperties:eventName :eventProps];

    // Test if we have an event name set.
    XCTAssertNotNil(event.event);
    // Test if the set event name equals the specified event name.
    XCTAssertEqual(event.event, eventName);
    // Test if a timestamp has been set.
    XCTAssertNotNil(event.timestamp);
    // Test if props have been set
    XCTAssertNotNil(event.properties);
}

- (void)testInitEventToDict {
    NSString *eventName = @"testEvent";
    O2MEvent *event = [[O2MEvent alloc] init:eventName];

    NSDictionary *eventDict = [event toDict];
    XCTAssertNotNil(eventDict[@"event"]);
    XCTAssertEqual(eventDict[@"event"], eventName);
    XCTAssertNotNil(eventDict[@"timestamp"]);
}

- (void)testInitEventWithParamsToDict {
    NSString *eventName = @"testEvent";
    NSDictionary *eventProps = @{
                                 @"foo": @"bar"
                                 };
    O2MEvent *event = [[O2MEvent alloc] initWithProperties:eventName :eventProps];

    NSDictionary *eventDict = [event toDict];
    XCTAssertNotNil(eventDict[@"event"]);
    XCTAssertEqual(eventDict[@"event"], eventName);
    XCTAssertEqual(eventDict[@"properties"][@"foo"], eventProps[@"foo"]);
    XCTAssertNotNil(eventDict[@"timestamp"]);
}

@end
