# Data specification
The data is sent as JSON data. The format contains two main properties, the `application` and the `tracked` property. Both properties are guaranteed to be included.

```
object {
	object application;
	array tracked { object; };
}
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
