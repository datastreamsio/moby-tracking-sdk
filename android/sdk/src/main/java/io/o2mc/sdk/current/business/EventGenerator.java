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

    private final DeviceInformation deviceInformation;

    public EventGenerator(DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public Event generateClickedButtonEvent(Button button, @Nullable Object extra) {
        if (extra == null) {
            return new Event(deviceInformation, Event.CLICKED_BUTTON, "some value when clicked button" + button.getId());
        } else {
            Log.i(TAG, extra.toString());
            return new Event(deviceInformation, Event.CLICKED_BUTTON, "some value when clicked button" + button.getId());
        }
    }
}
