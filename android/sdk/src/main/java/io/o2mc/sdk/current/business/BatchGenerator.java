package io.o2mc.sdk.current.business;

import android.util.Log;

import java.sql.Timestamp;
import java.util.List;

import io.o2mc.sdk.current.domain.Batch;
import io.o2mc.sdk.current.domain.DeviceInformation;
import io.o2mc.sdk.current.domain.Event;

/**
 * Generates batches of events.
 * Does this based on input events and adds meta data for analytical purposes.
 */
public class BatchGenerator {

    private static final String TAG = "BatchGenerator";

    private final DeviceInformation deviceInformation;

    public BatchGenerator(DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public Batch generateBatch(List<Event> events) {
        Log.i(TAG, "generateBatch: Generating batch");

        return new Batch(deviceInformation, Util.generateTimestamp(), events);
    }
}
