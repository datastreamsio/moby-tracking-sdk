# Data specification
The data is sent as JSON data. The format contains two main properties, the [`deviceInformation`](#deviceinformation-property) and the [`events`](#events-property) property. Both properties are guaranteed to be included.

```
object {
	object device;
	array events { object; };
	int number;
	int retries;
	string timestamp;
}
```

### Device property
This object contains generic device information.

```
object {
	string appId;
	string name;
	string os;
	string osVersion;
}
```

#### appId

The unique identifier for an app. Also known as bundle identifier or package name. An example appId be: `com.carrotstore.orderapp`.


#### name

The raw device model name.

*note: will be mapped to marketing device model names on the backend.*

#### os

The device's operating system. For example `iOS` or `android`.

#### osVersion

The operating system's version. For example `11.4` or `9`.

### Events property
This object is generated after a [`track()`](API.md#track) [`trackWithProperties()`](API.md#trackwithproperties) method has been triggered.

```
object {
	string event;
	object? properties;
	string timestamp;
}
```

#### event

Name of the triggered event.

#### properties

Optional custom properties object. Properties can be set using the [`trackWithProperties()`](API.md#trackwithproperties) method.

#### timestamp

Event generation time as an ISO 8601 format.

### Meta properties
These properties contain meta information about the current batch.

```
object {
	...
	int number;
	int retries;
	string sessionId;
	string timestamp;
}
```

#### number

Batch number. Numbering starts on `0` and increments each batch. The batch number will reset each session.

#### retries

Defines how often the dispatcher has been attempted to sent the batch to the backend. If the `retries` value reaches the maximum retry amount, the dispatcher will stop attempt to send the batch.

#### sessionId

Unique identifier to identify events across batches. There are 3 modes; anonymous, session identifier, persistent identifier.

The anonymous mode will result in a `null` value. The session identifier is a random `UUID` generated on each SDK initialisation. The persistent identifier can be any string value set by the implementing party.

#### timestamp

Batch generation time as an ISO 8601 format.
