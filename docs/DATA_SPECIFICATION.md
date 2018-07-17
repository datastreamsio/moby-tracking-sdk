# Data specification
The data is sent as JSON data. The format contains two main properties, the `deviceInformation` and the `events` property. Both properties are guaranteed to be included.

```
object {
	object deviceInformation;
	array events { object; };
	int number;
	int retries;
	string timestamp;
}
```

### DeviceInformation property


```
object {
	string appId;
	string os;
	string osVersion;
}
```

#### appId

The unique identifier for an app. Also known as bundle identifier or package name. An example appId be: `com.carrotstore.orderapp`.

### Events property

*TODO*

### Meta properties

```
object {
	...
	int number;
	int retries;
	string timestamp;
}
```