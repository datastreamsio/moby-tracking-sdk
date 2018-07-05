# Mobile tracking SDK

O2MC's mobile tracking SDK for collecting and measuring analytical events.

## Getting started

These instructions will get you up and running on your local machine. There is an Android and iOS app available for local testing.

### Prerequisites
The following tools are required depending on which mobile OS you are targeting.

**Android**

* Android SDK platform-tools 26

**iOS**

* XCode
* iOS 10+



## Installation

### Android
> Hello, World!

For a Hello-world example, please refer to [this](docs/ANDROID_HELLO_WORLD.md) page.

Refer to the [API documentation](docs/API.md) for more details on how to use the SDK.

## Data specification
The data is sent as JSON data. The format contains two main properties, the `application` and the `tracked` property. Both properties are guaranteed to be included.

```
object {
	object application;
	array tracked { object; };
}
```

Refer to the [specification documentation](docs/DATA_SPECIFICATION.md) for more details.

