# API and SDKs
Find out how our API works and integrate our products quickly using SDKs for Android and iOS.

## API
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
Content-Type: application/json; charset=UTF-8

{
   "device":{
      "appId":"io.o2mc.app",
      "name":"Android SDK Built For X86",
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
   "sessionId": "6458def9-30b8-4591-9eb3-9e9881b8dc3f",
   "timestamp":"2018-07-06T12:25:28Z"
}
```

Click [here](DATA_SPECIFICATION.md) for a more elaborate data specification.

## SDKs

### Android

[Link to the Android SDK](ANDROID.md)

This document describes the way our SDK interfaces between anyone implementing it into their own app, and a backend. This is not a hello-world tutorial, please refer to the `docs/` directory for those.

### iOS

> To be implemented.
