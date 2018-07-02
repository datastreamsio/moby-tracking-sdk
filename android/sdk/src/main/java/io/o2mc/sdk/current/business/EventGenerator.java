package io.o2mc.sdk.current.business;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import io.o2mc.sdk.current.domain.Event;

/**
 * Generates the appropriate event based on input.
 */
public class EventGenerator {

    private static final String TAG = "EventGenerator";

    public Event generateEvent(String eventName) {
        Log.i(TAG, String.format("generateEvent: Generated event with name '%s'", eventName));
        return new Event(eventName, null, Util.generateTimestamp());
    }

    public Event generateEventWithProperties(String eventName, String properties) {
        Log.i(TAG, String.format("generateEventWithProperties: Generated event '%s' with properties consisting of '%s' characters", eventName, properties.length()));
        return new Event(eventName, properties, Util.generateTimestamp());
    }
}
