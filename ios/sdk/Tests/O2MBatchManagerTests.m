//
//  O2MBatchManagerTests.m
//  Tests
//
//  Created by Tim Slot on 04/09/2018.
//  Copyright © 2018 Adversitement. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "O2MBatchManager.h"
#import "O2MEvent.h"

@interface O2MBatchManagerTests : XCTestCase

@property O2MBatchManager* batchManager;

@end

@implementation O2MBatchManagerTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
    self.batchManager = [[O2MBatchManager alloc] init];
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];

    self.batchManager = nil;
}

- (void)testDispatching {
    XCTAssertFalse([_batchManager isDispatching]);
    [_batchManager dispatchWithInterval:[[NSNumber alloc] initWithInt:30]];
    XCTAssertTrue([_batchManager isDispatching]);
    [_batchManager stop];
    XCTAssertFalse([_batchManager isDispatching]);
}

- (void) testAsyncConnectionRetryIncrement {
    int CONN_ATTEMPTS = (int)_batchManager.maxRetries;
    XCTAssertEqual(_batchManager.connRetries, 0);

    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing connection retry counter increments on failure"];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        XCTAssertEqual(self->_batchManager.connRetries, CONN_ATTEMPTS); // We should have counted all the failed attempts
        [expectation fulfill];
    });

    for(int i=0; i<CONN_ATTEMPTS; i++) {
        [_batchManager didDispatchWithError:nil];
    }

    [self waitForExpectationsWithTimeout:5 handler:nil];
}

- (void) testAsyncConnectionRetryCounterResetOnSuccess {
    XCTAssertEqual(_batchManager.connRetries, 0);

    XCTestExpectation *expectation = [self expectationWithDescription:@"Testing connection retry counter resets on success"];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        XCTAssertEqual(self->_batchManager.connRetries, 0);
        [expectation fulfill];
    });

    [_batchManager didDispatchWithError:nil];
    [_batchManager didDispatchWithSuccess:nil];
    [self waitForExpectationsWithTimeout:5 handler:nil];
}

- (void) testCreateBatchWithEvents {
    XCTAssertEqual(_batchManager.batchNumber, 0);

    NSArray *events = @[
                        [[O2MEvent alloc] init:@"foobar1"],
                        [[O2MEvent alloc] init:@"foobar2"],
                        ];
    XCTestExpectation *expectation = [self expectationWithDescription:@"Test if batch number increases"];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        XCTAssertEqual(self->_batchManager.batchNumber, 1);
        [expectation fulfill];
    });

    [_batchManager createBatchWithEvents:events];
    [self waitForExpectationsWithTimeout:5 handler:nil];
}

- (void) testStopDispatchingAfterRetryExceeded {
    int CONN_ATTEMPTS = (int)(_batchManager.maxRetries)+1;
    XCTAssertEqual(_batchManager.batchNumber, 0);
    XCTAssertEqual(_batchManager.connRetries, 0);
    NSArray *events = @[
                        [[O2MEvent alloc] init:@"foobar1"],
                        [[O2MEvent alloc] init:@"foobar2"],
                        ];
    XCTestExpectation *expectation = [self expectationWithDescription:@"Test if stops dispatching on retries exceeded"];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        XCTAssertEqual(self->_batchManager.batchNumber, 1);
        XCTAssertFalse(self->_batchManager.isDispatching);
        [expectation fulfill];
    });

    [_batchManager createBatchWithEvents:events];
    for(int i=0; i<CONN_ATTEMPTS; i++) {
        [_batchManager didDispatchWithError:nil];
    }
    [_batchManager dispatchWithInterval:[[NSNumber alloc] initWithInt:1]];
    XCTAssertTrue(self->_batchManager.isDispatching);
    [self waitForExpectationsWithTimeout:5 handler:nil];
}

- (void) testDidDispatchWithSuccess {
    XCTAssertEqual(_batchManager.batchNumber, 0);
    XCTAssertEqual(_batchManager.connRetries, 0);
    NSArray *events = @[
                        [[O2MEvent alloc] init:@"foobar1"],
                        [[O2MEvent alloc] init:@"foobar2"],
                        ];
    XCTestExpectation *expectation = [self expectationWithDescription:@"Test if stops dispatching on retries exceeded"];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        XCTAssertEqual(self->_batchManager.batchNumber, 1);
        [expectation fulfill];
    });

    [_batchManager createBatchWithEvents:events];
    [_batchManager didDispatchWithSuccess:nil];
    [self waitForExpectationsWithTimeout:5 handler:nil];
}

@end
