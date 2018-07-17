# Data specification
The data is sent as JSON data. The format contains two main properties, the `deviceInformation` and the `events` property. Both properties are guaranteed to be included.

```
object {
	object deviceInformation;
	array events { object; };
}
```

### DeviceInformation property


```
object {
	string os;
	string osVersion;
}
```
### Events property

*TODO*
