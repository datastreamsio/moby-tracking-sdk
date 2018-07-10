package io.o2mc.sdk.business.batch;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import java.util.Timer;

import io.o2mc.sdk.Config;
import io.o2mc.sdk.TrackingManager;
import io.o2mc.sdk.util.Util;
import io.o2mc.sdk.domain.Batch;
import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.BuildConfig;

public class BatchManager {

    private static final String TAG = "BatchManager";

    private String endpoint;
    private boolean usingHttpsEndpoint;

    private int maxRetries; // max amount of times to retry sending batches

    private final Timer timer = new Timer(); // timer used for dispatching events
    private int dispatchInterval; // interval on which to send events

    private TrackingManager trackingManager;

    private BatchBus batchBus;

    public BatchManager(TrackingManager trackingManager, String endpoint, int dispatchInterval, int maxRetries) {
        this.batchBus = new BatchBus();

        setEndpoint(endpoint);
        setDispatchInterval(dispatchInterval);
        setMaxRetries(maxRetries);

        this.trackingManager = trackingManager;

        BatchDispatcher.getInstance().setBatchManager(this);
    }

    /**
     * Sets the max amount of retries for generating batches. Helps to reduce cpu usage / battery draining.
     */
    private void setMaxRetries(int maxRetries) {
        if (Util.isValidMaxRetries(maxRetries)) {
            this.maxRetries = maxRetries;
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, String.format("O2MC: Max retries amount '%s' is invalid.", maxRetries));
                Log.w(TAG, String.format("setMaxRetries: Setting to default '%s'", Config.DEFAULT_MAX_RETRIES));
            }
            this.maxRetries = Config.DEFAULT_MAX_RETRIES;
        }
    }

    /**
     * Tells the EventManager on which intervals it should send the generated events.
     * Starts dispatching once interval is set.
     *
     * @param seconds time in seconds
     */
    private void setDispatchInterval(int seconds) {
        if (Util.isValidDispatchInterval(seconds)) {
            this.dispatchInterval = seconds;
            startDispatching(); // Interval is set, start dispatching now.
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, String.format("O2MC: Dispatch interval '%s' is invalid. Note that the value must be positive and is denoted in seconds.%nNot dispatching events.", dispatchInterval));
                Log.w(TAG, String.format("setDispatchInterval: Setting to default '%s'", Config.DEFAULT_DISPATCH_INTERVAL));
            }
            this.dispatchInterval = Config.DEFAULT_DISPATCH_INTERVAL;
        }
    }

    /**
     * Checks whether or not the endpoint is a valid URL.
     *
     * @param endpoint URL in String format
     */
    private void setEndpoint(String endpoint) {
        if (endpoint == null || endpoint.isEmpty()) {
            if (BuildConfig.DEBUG) Log.e(TAG, "O2MC: Please provide a non-empty endpoint.");
        } else if (Util.isValidEndpoint(endpoint)) {
            this.endpoint = endpoint;
            this.usingHttpsEndpoint = Util.isHttps(endpoint);
        } else {
            if (BuildConfig.DEBUG)
                Log.e(TAG, String.format("O2MC: Endpoint is incorrect. Tracking events will fail to be dispatched. Please verify the correctness of '%s'.", endpoint));
        }
    }

    /**
     * Called upon successful HTTP post
     */
    public void dispatchSuccess() {
        if (BuildConfig.DEBUG) Log.d(TAG, "Dispatch successful.");

        batchBus.lastBatchSucceeded();
    }

    /**
     * Called upon failure of HTTP post
     */
    public void dispatchFailure() {
        if (BuildConfig.DEBUG) Log.d(TAG, "Dispatch failure. Not clearing EventBus.");

        batchBus.lastBatchFailed();
    }

    /**
     * Sets a timer for dispatching events to the backend.
     */
    private void startDispatching() {
        // Check if the device is allowed to dispatch events
        if (!Util.isAllowedToDispatchEvents(usingHttpsEndpoint)) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "run: Not allowed to dispatch events. See previous message(s) for more info.");
            return;
        }

        timer.schedule(new DispatcherTask(), dispatchInterval * 1000, dispatchInterval * 1000);
    }

    /**
     * Retrieves all events which are currently in the EventBus.
     *
     * @return a list of events -- possibly empty
     */
    private List<Event> getEvents() {
        return trackingManager.getEventsFromBus();
    }

    /**
     * Clears all events which are currently in the EventBus.
     */
    private void clearEvents() {
        trackingManager.clearEventsFromBus();
    }

    public void reset() {
        batchBus.clearBatches();
        batchBus.clearPending();
    }

    /**
     * Sends all events from the EventBus to the backend, if there are any events.
     */
    class DispatcherTask extends TimerTask {
        private static final String TAG = "Dispatcher";

        @Override
        public void run() {
            // Initialize batchGenerator meta data
            batchBus.setDeviceInformation(trackingManager.getDeviceInformation());

            // Don't try resending a batch if the max retries limit has exceeded
            if (batchBus.getRetries() > maxRetries) {
                if (BuildConfig.DEBUG)
                    Log.w(TAG, "run: Max retries limit has been reached. Not trying to resend batch.");
                timer.cancel();
                timer.purge();
                return;
            }

            // Only generate a batch if we have events
            if (getEvents().size() > 0) {
                batchBus.add(batchBus.generateBatch(getEvents()));
                if (BuildConfig.DEBUG)
                    Log.d(TAG, String.format("run: Newly generated batch contains '%s' events", getEvents().size()));
                clearEvents();
            } else {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "run: There are no events to generate a new batch from.");
            }

            // If there's a batch pending, skip this run
            if (batchBus.awaitingCallback()) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "run: Still awaiting a callback from previous run. Stopping here.");
                return;
            }

            // There's no pending batch, set / generate one if possible
            if (batchBus.getPendingBatch() == null) {
                batchBus.setPendingBatch();
            }

            // If there is one now, send it
            if (batchBus.getPendingBatch() == null) {
                if (BuildConfig.DEBUG)
                    Log.i(TAG, "run: There is no pending batch set. Not dispatching.");
                return;
            }

            // Dispatch the newly set batch
            if (BuildConfig.DEBUG)
                Log.i(TAG, String.format("run: Dispatching batch with '%s' events.", batchBus.getPendingBatch().getEvents().size()));
            batchBus.onDispatch();
            BatchDispatcher.getInstance().post(endpoint, batchBus.getPendingBatch());
        }
    }
}
