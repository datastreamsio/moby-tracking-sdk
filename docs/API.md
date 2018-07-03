# Public API: IOS & Android
The following functions can be executed to send data to the O2MC Platform and to configure how this is done. A typical implementation only uses the tracking ing function that will send data to the end point. Other functions defined here allow additional configuration and set up.

# General tracking function
**Send key value pair to endpoint (make sure second argument is a string and not a javascript object (Use JSON.stringify())**

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
