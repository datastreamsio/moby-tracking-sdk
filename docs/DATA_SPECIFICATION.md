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

#### os

The device's operating system. For example `ios` or `android`. The value should be in lowercase.

#### osVersion

The operating system's version. For example `11.4` or `9`.

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