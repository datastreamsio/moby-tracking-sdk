# Initialization & Configuration

This is how you'd initialize our O2MC module:

`[[O2MC sharedInstance] setEndpoint:@"<endpoint>"];`

Please consider defining the development or production URL based on the build configuration.

Optionally, you could specify the `dispatchInterval` in **seconds** yourself (default **10**):

> Change dispatch interval to 60 seconds

`[[O2MC sharedInstance] setDispatchInterval:[[NSNumber alloc] initWithInt:60]];`


# General tracking functions

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

