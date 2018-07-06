# Public API: IOS & Android
The following functions can be executed to send data to the O2MC Platform and to configure how this is done. A typical implementation only uses the tracking function that will send data to the end point. Other functions defined here allow additional configuration and set up.

# General tracking function
**Send key value pair to endpoint (make sure second argument is a string and not a javascript object (Use JSON.stringify()))**

```Void trackWithproperties(String eventName, String propertiesAsJson)```

The parameter `eventName` can be any string. Similarly the only requirement to the second parameter is that a JSON formatted object is used, there is no limitation to the name or structure of the properties. (For the application receiving and processing the data, the specification of the data set needs to be made provided so the data can be processed properly).

**Send single string to endpoint**

```Void Track(String eventName)```

This tracking function only captures the eventname (and only the automatically collected metadata)

# Configuration functions
**Change endpoint for the data funnel. (Http url)**

```Void setEndpoint(String endpoint)```

Note that the initialisation of the tracker object already defines an endpoint so if no change to this endpoint is required, this function does not need to be used to send data

**Sets the dispatch interval**

```Void setDispatchInterval(int interval)```

The parameter defines the interval in minutes for buffering events and sending to the processing server

# API Calls
The functions and the SDK calls result in a HTTPS POST request. If for some reason it is not possible or not desirable to use the SDK, a direct call can be made to the end point. The following URL structure should be adhered to:

```https://<SERVER>/flow/code.js?dimml.concept=//<ENVIRONMENT>@<FILE>/<CONCEPT>```

- Server points to the specific location of the processing environment. 
- Environment is defined as either prod, QA, dev or user specific test environments. If the O2MC Cloud environment is used, then the value can be collected by using the O2MC Studio and view the Public API at the properties of the DimML file created for processing the data.
- File is the exact name of the DimML file created for processing the data based on the type of connection and connection details (e.g. domain on a website).
- Concept is the name of the concept in the DimML file which should process the data. This allows data to be send to a specific concept/component of the application.

For example if you have used the O2MC Studio to create an file exampleandroid.app.dimml with concept Global in the test environment for processing the data being the user johndoe, the HTTP end point to use in the native app implementation is

```/user/{user}/items/{item}/flow/code.js?dimml.concept=//johndoe@exampleandroid.app.dimml```

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
