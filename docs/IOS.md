# Initialization & Configuration

This is how you'd initialize our O2MC module:

`[[O2MC sharedInstance] setEndpoint:@"<endpoint>"];`

Please consider defining the development or production URL based on the build configuration.

## setDispatchInterval

Configures the dispatch interval in seconds (default **10**). Defining a lower interval results in more realtime and lower chance of data loss. On the other hand a lower interval will result in more data usage and resource usage from the end user's device.

> Change dispatch interval in seconds

`[[O2MC sharedInstance] setDispatchInterval:<#(NSInteger)#>]`

## setMaxRetries

The max amount of connection retries before stopping dispatching (defaults to **5**).

> Changing the max amount of retries

`[[O2MC sharedInstance] setMaxRetries:<#(NSInteger)#>]`

# Control methods

## stop

Stops tracking of events.

> Stop tracking

`[[O2MC sharedInstance] stop]`

Optionally the stop method also accepts a BOOL. If true, the currently queued events will be removed.

> Stop and remove queued events

`[[O2MC sharedInstance] stop:YES]`

## resume

Resumes tracking when stopped.

> Resume

`[[O2MC sharedInstance] stop:YES]`

# General tracking methods

## [track:..]

```objective-c
/**
 * Tracks an event.
 * Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
 * @param eventName name of tracked event
 */
-(void)track:(nonnull NSString*)eventName;
```

> Invoke method by executing the following statement

`[[O2MC sharedInstance] track:@"<eventname>"];`

## [trackWithProperties:..]

```objective-c
/**
 * Tracks an event with additional data.
 * Essentially adds a new event with the String parameter as name and any additonal properties.
 * @param eventName name of tracked event
 * @param properties anything you'd like to keep track of
 */
-(void)trackWithProperties:(nonnull NSString*)eventName properties:(nonnull NSDictionary*)properties;
```

> Invoke method by executing the following statement

`[[O2MC sharedInstance] trackWithProperties:@"<eventname>" properties:@{@"<prop1>": @1, @"<prop2>": @2}];`

