package io.o2mc.sdk.current.business;

import android.bluetooth.BluetoothClass;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import io.o2mc.sdk.current.domain.DeviceInformation;
import io.o2mc.sdk.current.domain.Event;

/**
 * Generates the appropriate event based on input.
 */
public class EventGenerator {

    private static final String TAG = "EventGenerator";

    public Event generateClickedButtonEvent(Button button, @Nullable Object extra) {
        Log.i(TAG, "generateClickedButtonEvent: Generated clickedButtonEvent");
        if (extra == null) {
            return new Event(Event.CLICKED_BUTTON, "some value when clicked button" + button.getId());
        } else {
            return new Event(Event.CLICKED_BUTTON_EXTRA, "some value when clicked button" + button.getId());
        }
    }
}
