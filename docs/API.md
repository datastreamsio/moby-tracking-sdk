# Public API: IOS & Android
> Scope
This document describes the way our SDK interfaces between anyone implementing it into their own app, and a backend. This is not a hello-world tutorial, please refer to the `docs/` directory for those.

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


# API Calls
The functions and the SDK calls result in a HTTPS POST request. If for some reason it is not possible or not desirable to use the SDK, a direct call can be made to the end point. The following URL structure should be adhered to:

```https://<SERVER>/flow/code.js?dimml.concept=//<ENVIRONMENT>@<FILE>/<CONCEPT>```

- Server points to the specific location of the processing environment. 
- Environment is defined as either prod, QA, dev or user specific test environments. If the O2MC Cloud environment is used, then the value can be collected by using the O2MC Studio and view the Public API at the properties of the DimML file created for processing the data.
- File is the exact name of the DimML file created for processing the data based on the type of connection and connection details (e.g. domain on a website).
- Concept is the name of the concept in the DimML file which should process the data. This allows data to be send to a specific concept/component of the application.

For example if you have used the O2MC Studio to create an file exampleandroid.app.dimml with concept Global in the test environment for processing the data being the user johndoe, the HTTP end point to use in the native app implementation is

```https://baltar-dev.dimml.io/flow/code.js?dimml.concept=//johndoe@exampleandroid.app.dimml```

Additionally sending the data requires setting the `Content-Type` header to `application/json`. 

Here's a complete example of the HTTP request.

```http
POST /flow/code.js?dimml.concept=//johndoe@exampleandroid.app.dimml HTTP/1.1
Host: https://baltar-dev.dimml.io
Content-Type: application/json

{
   "deviceInformation":{
      "appId":"io.o2mc.app",
      "deviceId":"eeb261ced725039b",
      "deviceName":"Android SDK Built For X86",
      "os":"android",
      "osVersion":"9"
   },
   "events":[
      {
         "name":"MainActivityCreated",
         "timestamp":"2018-07-06T12:25:18Z"
      },
      {
         "name":"Clicked button: \u0027Create Track Event\u0027",
         "timestamp":"2018-07-06T12:25:23Z",
         "value":"Name"
      },
      {
         "name":"Clicked button: \u0027Create Track Event\u0027",
         "timestamp":"2018-07-06T12:25:26Z",
         "value":"Name"
      }
   ],
   "number":0,
   "retries":0,
   "timestamp":"2018-07-06T12:25:28Z"
}
```
