# iOS

This folder contains the iOS [SDK](sdk) and an [example app](app-obj-c/) written in objective-c.

## Configuration

### View tracking

The SDK can automatically track view changes by hooking into `UIViewController`'s `viewDidAppear` and  `viewWillDisappear` methods.

View tracking isn't enabled by default. It can be enabled by defining `O2M_TRACK_VIEWS` as a boolean in `Info.plist` and set it to `YES`.

*note: at the moment it only works for objective-c apps*