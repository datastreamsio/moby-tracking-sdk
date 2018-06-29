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



## Installation

A step by step series of instructions that tell you how to include the SDK into your app.

### Android

There are two methods to install the SDK for Android. The first one is to include the debug aar module and the second one is to compile the source code together with your app.


#### Install using the aar module file

* Add the `sdk-debug.aar` into your project's `libs/` directory.
* Add to following flatDir section to your top level gradle file's repositories section:

```
allprojects {
	repositories {
		...
		flatDir { 
		  dirs 'libs'
		}
	}
}
```
* Add the following dependency to your app's build.gradle file:

```
dependencies {
	...
	compile 'com.squareup.okhttp3:okhttp:3.10.0'
	compile 'com.jaredrummler:android-device-names:1.1.7'
	compile(name: 'sdk-debug', ext: 'aar')
}
```

*The sdk-debug.aar can be found in the app/libs directory*

#### Install by compiling the sdk's source code
* Copy the sdk folder to your Android project
* Add `':sdk'` to your settings.gradle
* Add the following dependency to your app's build.gradle file:

```
dependencies {
	...
	compile project(":sdk")
}
```

## Usage


## Android

To initiate the library and use the basic tagging functionality you need to create a new instance of it.

This can be done the following way:  

```
public class App extends Application {

    private static O2MC o2mc;
    private static Application instance;

    public App(){
        o2mc = new O2MC(this, <HTTP_ENDPOINT>);
        o2mc.tracker.setDispatchInterval(10);
    }
}  
```

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

