# Mobile tracking SDK

O2MC's mobile tracking SDK for collecting and measuring analytical events.

## Getting started

These instructions will get you up and running on your local machine. There is an Android and iOS app available for local testing.

### Prerequisites
The following tools are required depending on which mobile OS you are targeting.

**Android**

* Android SDK platform-tools 26

**iOS**

* XCode 9+
* iOS 9+



## Installation

### Android
> Hello, World!

For a Hello-world example, please refer to [this](docs/ANDROID_HELLO_WORLD.md) page.

Refer to the [API documentation](docs/API.md) for more details on how to use the SDK.

## Data specification
The data is sent as JSON data. The format contains two main properties, the `deviceInformation` and the `events` property. Both properties are guaranteed to be included.

> Example
```json
{
    "deviceInformation": {
        "appId": "io.o2mc.app",
        "deviceName": "Android SDK built for x86",
        "os": "android",
        "osVersion": "9"
    },
    "events": [
        {
            "name": "MainActivity created",                                                     
            "timestamp": "2018-07-25T11:19:17+02:00",
        },
        {
            "name": "Clicked button: 'Create Track Event'",                                                     
            "timestamp": "2018-07-25T11:19:17+02:00",
            "value": "Name"
        }
    ],
    "number": 1,
    "retries": 0,
    "timestamp": "2018-07-25T11:19:18+02:00"
}
```

Refer to the [specification documentation](docs/DATA_SPECIFICATION.md) for more details.

## Contributing

Any kind of contribution is welcome.
For bug reports, feature requests, please open an issue without any hesitation.
For code contributions, it's strongly suggested to open an issue for discussion first. For more details, please refer to [contributing documentation](docs/CONTRIBUTING.md).

## License

[MIT license](LICENSE).

Copyright (c) Insite Innovations and Properties B.V.
