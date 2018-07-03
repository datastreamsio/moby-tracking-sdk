package io.o2mc.sdk.business;

import android.util.Log;

import java.util.List;

import io.o2mc.sdk.domain.Batch;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.domain.Event;

/**
 * Generates batches of events.
 * Does this based on input events and adds meta data for analytical purposes.
 */
public class BatchGenerator {

    private static final String TAG = "BatchGenerator";

    private static int batchCounter = 0;

    private DeviceInformation deviceInformation;

    /**
     * Tells whether or not we're about to run for the first time.
     *
     * @return true if we're about to run for the first time.
     */
    public boolean firstRun() {
        return batchCounter <= 0;
    }

    public void setDeviceInformation(DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public Batch generateBatch(List<Event> events) {
        Log.i(TAG, "generateBatch: Generating batch");

        return new Batch(
                deviceInformation,
                Util.generateTimestamp(),
                events,
                batchCounter++ /*add 1 to the counter after this statement*/
        );
    }
}
