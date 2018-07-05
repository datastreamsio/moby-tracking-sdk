package io.o2mc.sdk.business;

import android.util.Log;

import io.o2mc.sdk.Util;
import io.o2mc.sdk.domain.Event;

/**
 * Generates the appropriate event based on input.
 */
public class EventGenerator {

    private static final String TAG = "EventGenerator";

    public Event generateEvent(String eventName) {
        Log.i(TAG, String.format("generateEvent: Generated event with name '%s'", eventName));
        return new Event(eventName, null, Util.generateTimestamp());
    }

    public Event generateEventWithProperties(String eventName, String value) {
        Log.i(TAG, String.format("generateEventWithProperties: Generated event '%s' with value consisting of '%s' characters", eventName, value.length()));
        return new Event(eventName, value, Util.generateTimestamp());
    }
}
