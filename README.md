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

## Data structure
The data is sent as JSON data. The format contains two main properties, the `application` and the `tracked` property. Both properties are guaranteed to be included.

```
object {
	object application;
	array tracked { object; };
},
```

### Application property


```
object {
	string os;
	string osVersion;
}
```
### Tracked property

*TODO*

## Public API: React-native, IOS & Android
**All events automatically get a timestamp assigned**
**The system automatically gathers general phone data.** (Screensize, Android OS version, Phone type)

**Send single string to endpoint**
```Void Track(String eventName)```

**Send key value pair to endpoint (make sure second argument is a string and not a javascript object (Use JSON.stringify())**
```Void trackWithproperties(String eventName, String propertiesAsJson)```

**Give the user an alias, can be used to match an anonymous user.**
```Void createAlias(String alias)```

**Couple an identifier to a user. (Only set once. for example when an user authenticates) When not set the system automatically assigns an uuid at initialisation).**
```Void identify(String alias)```

**Start an eventtimer coupled to an eventName.**
```Void timeEventStart(String eventName)```

**Stop an eventtimer coupled to eventname. Must be the same as the eventName used at timeEventStart**
```Void timeEventStop(String eventName)```

**Start an eventtimer coupled to an eventName.**
```Void timeEventStartWithProps(String eventName, String propertiesAsJson)```

**Stop an eventtimer coupled to eventname. Must be the same as the eventName used at timeEventStart**
```Void timeEventStopWithProps(String eventName, String propertiesAsJson)```

**Reset the datafunnel. Removes all gathered data from memory. (Be carefull using this method. it might remove unsent data)**
```Void reset()```  

**Set an endpoint for the data funnel. (Http url)**
```Void setEndpoint(String endpoint)```  

**Sets the dispatchinterval**
```Void setDispatchInterval(int interval)```

