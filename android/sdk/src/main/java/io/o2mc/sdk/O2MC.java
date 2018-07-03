package io.o2mc.sdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import io.o2mc.sdk.business.BatchGenerator;
import io.o2mc.sdk.business.DeviceManager;
import io.o2mc.sdk.business.EventBus;
import io.o2mc.sdk.business.EventDispatcher;
import io.o2mc.sdk.business.EventGenerator;
import io.o2mc.sdk.business.Util;
import io.o2mc.sdk.domain.Batch;
import io.o2mc.sdk.domain.Event;

/**
 * This is central point of communication between the SDK and the app implementing it.
 * The implementing app should never have anything to deal with any other class than this one.
 */
public class O2MC implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "O2MC";

    @SuppressWarnings("FieldCanBeLocal")
    private Application app; // required for activity callbacks & context (may need it for more callbacks in the future)

    private String endpoint;

    private Timer timer = new Timer(); // timer used for dispatching events
    private int dispatchInterval; // interval on which to send events

    private DeviceManager deviceManager;
    private EventGenerator eventGenerator;
    private BatchGenerator batchGenerator;
    private EventBus eventBus;

    public O2MC(Application app, String endpoint) {
        if (app == null) {
            Log.w(TAG, "O2MC: Application (context) provided was null. " +
                    "Manually tracked events will still work, however " +
                    "activity lifecycle callbacks will not be automatically detected.");
        } else {
            this.app = app;
            this.app.registerActivityLifecycleCallbacks(this);
        }

        if (endpoint == null || endpoint.isEmpty()) {
            Log.e(TAG, "O2MC: Please provide a non-empty endpoint.");
        } else if (Util.isValidEndpoint(endpoint)) {
            this.endpoint = endpoint;
        } else {
            Log.e(TAG, String.format("O2MC: Endpoint is incorrect. Tracking events will fail to be dispatched. Please verify the correctness of '%s'.", endpoint));
        }

        this.deviceManager = new DeviceManager(app);
        this.eventGenerator = new EventGenerator();
        this.batchGenerator = new BatchGenerator();
        this.eventBus = new EventBus();

        EventDispatcher.getInstance().setO2mc(this);
    }

    /**
     * Tells the EventManager on which intervals it should send the generated events.
     * Starts dispatching once interval is set.
     *
     * @param seconds time in seconds
     */
    public void setDispatchInterval(int seconds) {
        if (Util.isValidDispatchInterval(seconds)) {
            this.dispatchInterval = seconds;
            startDispatching(); // Interval is set, start dispatching now.
        } else {
            Log.e(TAG, String.format("O2MC: Dispatch interval '%s' is invalid. Note that the value must be positive and is denoted in seconds.%nNot dispatching events.", dispatchInterval));
        }
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityCreated' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.i(TAG, "Activity created.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityStarted' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityStarted(Activity activity) {
        Log.d(TAG, "Activity started.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityStarted' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "Activity resumed.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityPaused' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "Activity resumed.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityStopped' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityStopped(Activity activity) {
        Log.d(TAG, "Activity stopped.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivitySaveInstanceState' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d(TAG, "Activity saved instance state.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityDestroyed' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "Activity destroyed.");
    }

    /**
     * Tracks an event.
     * Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
     *
     * @param eventName name of tracked event
     */
    public void track(String eventName) {
        Log.d(TAG, String.format("Tracked '%s'", eventName));
        Event e = eventGenerator.generateEvent(eventName);
        eventBus.add(e);
    }

    /**
     * Tracks an event with additional data.
     * Essentially adds a new event with the String parameter as name and any properties in String format.
     * Will be dispatched to backend on next dispatch interval.
     *
     * @param eventName name of tracked event
     * @param value     anything you'd like to keep track of in String format
     */
    public void trackWithProperties(String eventName, String value) {
        Log.d(TAG, String.format("Tracked '%s'", eventName));
        Event e = eventGenerator.generateEventWithProperties(eventName, value);
        eventBus.add(e);
    }

    /**
     * Called upon successful HTTP post
     */
    public void dispatchSuccess() {
        Log.d(TAG, "Dispatch successful.");
        reset();
    }

    /**
     * Removes all tracking events which would otherwise be sent upon next dispatch interval.
     */
    public void reset() {
        eventBus.clearEvents();
    }

    /**
     * Called upon failure of HTTP post
     */
    public void dispatchFailure() {
        Log.d(TAG, "Dispatch failure. Not clearing EventBus.");
    }

    /**
     * Sets a timer for dispatching events to the backend.
     */
    private void startDispatching() {
        timer.schedule(new Dispatcher(), dispatchInterval * 1000, dispatchInterval * 1000);
    }

    /**
     * Sends all events from the EventBus to the backend, if there are any events.
     */
    class Dispatcher extends TimerTask {
        private static final String TAG = "Dispatcher";

        public void run() {
            // Don't dispatch if we have no events
            if (eventBus.getEvents().size() <= 0) {
                Log.d(TAG, "run: There are no events to dispatch. Skipping.");
                return;
            }

            // Initialize batchGenerator meta data on the first run
            if (batchGenerator.firstRun()) {
                batchGenerator.setDeviceInformation(deviceManager.generateDeviceInformation());
            }

            Log.i(TAG, String.format("run: Dispatching batch with '%s' events.", eventBus.getEvents().size()));
            Batch b = batchGenerator.generateBatch(eventBus.getEvents());
            EventDispatcher.getInstance().post(endpoint, b);
        }
    }
}
