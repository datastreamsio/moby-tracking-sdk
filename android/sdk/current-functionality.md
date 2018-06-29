# Current application functionality

## 1
Opening the app registers an event:
  "application": {
        "AppID": "io.o2mc.app",
        "batch": 0,
        "device": "Android SDK Built For X86",
        "deviceID": "eeb261ced725039b",
        "os": "android",
        "osVersion": "9"
    },
    "tracked": [
        {
            "elementValue": "{\"event\":\"MainActivityCreated\",\"alias\":\"0a7a1f00-5ab6-4d99-b381-5fe8271f4f27\",\"identity\":\"\",\"time\":\"2018-06-29 18:07:19.243\"}",
            "eventType": "MainActivityCreated",
            "trackingDateTime": "2018-06-29T16:07:29Z"
        },
        {
            "elementValue": "{\"alias\":\"0a7a1f00-5ab6-4d99-b381-5fe8271f4f27\",\"identity\":\"\"}",
            "eventType": "alias",
            "trackingDateTime": "2018-06-29T16:07:29Z"
        }
    ]
This event is sent after some specified time, 10 by default.

## 2
Entering something into the text field and hitting 'create track event' adds an event to the bus.
This event is then, on the interval, sent again. This time it contains:
{
    "application": {
        "AppID": "io.o2mc.app",
        "batch": 1,
        "device": "Android SDK Built For X86",
        "deviceID": "eeb261ced725039b",
        "os": "android",
        "osVersion": "9"
    },
    "tracked": [
        {
            "elementValue": "{\"event\":\"Nameee\",\"alias\":\"0a7a1f00-5ab6-4d99-b381-5fe8271f4f27\",\"identity\":\"\",\"time\":\"2018-06-29 18:10:11.626\"}",
            "eventType": "Nameee",
            "trackingDateTime": "2018-06-29T16:10:19Z"
        }
    ]
}
