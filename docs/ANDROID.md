# Hello World
> A 'Hello, world!" example on how to use the SDK.

## What is this?
A tutorial aimed at tracking your first event using the SDK. 
This is not a tutorial aimed at learning how to create Android Apps. Some Android development experience is expected.

> **Attention**: Please note that there already is a working example app provided in this repository. This tutorial is meant to be used as an additional from-scratch introduction to the SDK.

## Getting started
These instructions will get you up and running on your local machine, targeting the Android platform.

### Prerequisites
- Any editor capable of creating and compiling Android probjects ([Android Studio](https://developer.android.com/studio/) is recommended).
- Android SDK platform-tools 26 (included in recommended installation process of Android Studio)
- One of either:
	- [Android Virtual Device](https://developer.android.com/studio/run/managing-avds)
	- A [USB connected Android debuggable device](https://developer.android.com/studio/run/device)
- This tutorial assumes you have a back-end running to accept tracking events. You can find some examples [here](../backend/).

# Steps
> A step by step series of instructions that tell you how to include the SDK into your app.

## 1. Project

Create a new project, or use an existing one. Make sure there's at least one activity being started on run.

## 2. Obtain SDK

There are two methods to install the SDK for Android. The first one is to include the debug aar module and the second one is to compile the source code together with your app.

#### Install using the aar module file

*The sdk-debug.aar can be found in the app/libs directory*

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
	implementation 'com.google.code.gson:gson:2.8.5'
	implementation 'com.squareup.okhttp3:okhttp:3.10.0'
	implementation 'com.jaredrummler:android-device-names:1.1.7'
	implementation(name: 'sdk-debug', ext: 'aar')
}
```

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

# 3. Initialize the SDK

Create a new Java class named `App.java`. Make it extend `Application`.

The class should look like this after imports/package declaration.

```
...

import android.app.Application;

import io.o2mc.sdk.O2MC;

public class App extends Application {

    private static O2MC o2mc;

    public App() {
        o2mc = new O2MC(this, "http://10.0.2.2:5000/events"); // TODO: replace the endpoint with your own URL
    }

    public static O2MC getO2mc() {
        return o2mc;
    }
}
```

In the `AndroidManifest.xml`, add the line `android:name=".App"` in the `Application` tag:

```
...
    <application
        android:name=".App"
	...
    </application>
...
```

# 4. Start Tracking!

From any activity/fragment, use any of the following methods to start tracking events:
```
App.getO2mc().track("Hello, World!");
```

# Initialization & Configuration

In its simplest form, this is how you'd initialize our O2MC module:

`o2mc = new O2MC(this, "<endpoint>");`

Optionally, you could specify the `dispatchInterval` in **seconds** yourself (default **10**):

`o2mc = new O2MC(this, "<endpoint>", 15);`

Additionally, you could specify the max amount of retries before the SDK gives up on retrying to send events. After the specified amount of times (default **15**), the SDK will no longer try to send events, thus saving battery and data usage wisely.

`o2mc = new O2MC(this, "<endpoint>", 15, 20);`

Finally, it's possible to set a max retries number without explicitly stating the `dispatchInterval`, like this:

`o2mc.setMaxRetries(15);`


# General tracking function

## track(...)

```java
/**
* Tracks an event.
* Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
*
* @param eventName name of tracked event
*/
public void track(String eventName) { ... }
```

> Invoke function by executing the following statement

`App.getO2mc().track("<eventname>");`

## trackWithProperties(...)

```java
/**
* Tracks an event with additional data.
* Essentially adds a new event with the String parameter as name and any properties in String format.
* Will be dispatched to backend on next dispatch interval.
*
* @param eventName name of tracked event
* @param value     anything you'd like to keep track of in String format
*/
public void trackWithProperties(String eventName, String value) { ... }
```

> Invoke function by executing the following statement

`App.getO2mc().trackWithProperties("<eventname>", "<eventvalue>");`
