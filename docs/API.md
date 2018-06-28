# Public API: React-native, IOS & Android
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