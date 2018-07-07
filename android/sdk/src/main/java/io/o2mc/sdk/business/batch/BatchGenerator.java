package io.o2mc.sdk.business.batch;

import android.util.Log;

import java.util.List;

import io.o2mc.sdk.BuildConfig;
import io.o2mc.sdk.TimeUtil;
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

    private int retries = 0; // represents the amount of times in a row batches have failed to be sent

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
        if (BuildConfig.DEBUG) Log.i(TAG, "generateBatch: Generating batch");

        return new Batch(
                deviceInformation,
                TimeUtil.generateTimestamp(),
                events,
                batchCounter++, /*add 1 to the counter after this statement*/
                retries
        );
    }

    /**
     * Called when the most recent batch has failed to be processed by the backend.
     */
    public void lastBatchFailed() {
        retries++;

        if (BuildConfig.DEBUG)
            Log.d(TAG, String.format("Last batch failed. Retries is '%s' now.", retries));
    }

    /**
     * Called when the most recent batch has successfully been processed by the backend.
     */
    public void lastBatchSucceeded() {
        retries = 0;

        if (BuildConfig.DEBUG)
            Log.d(TAG, String.format("Last batch succeeded. Retries is '%s' now.", retries));
    }

    public int getRetries() {
        return retries;
    }
}
