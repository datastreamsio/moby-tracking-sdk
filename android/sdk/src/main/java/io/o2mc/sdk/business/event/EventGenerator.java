package io.o2mc.sdk.business.event;

import android.util.Log;

import io.o2mc.sdk.BuildConfig;
import io.o2mc.sdk.TimeUtil;
import io.o2mc.sdk.domain.Event;

/**
 * Generates the appropriate event based on input.
 */
public class EventGenerator {

    private static final String TAG = "EventGenerator";

    public Event generateEvent(String eventName) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, String.format("generateEvent: Generated event with name '%s'", eventName));

        return new Event(eventName, null, TimeUtil.generateTimestamp());
    }

    public Event generateEventWithProperties(String eventName, String value) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, String.format("generateEventWithProperties: Generated event '%s' with value consisting of '%s' characters", eventName, value.length()));

        return new Event(eventName, value, TimeUtil.generateTimestamp());
    }
}
