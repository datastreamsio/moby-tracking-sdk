package io.o2mc.sdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import io.o2mc.sdk.business.batch.BatchDispatcher;
import io.o2mc.sdk.business.batch.BatchGenerator;
import io.o2mc.sdk.business.DeviceManager;
import io.o2mc.sdk.business.event.EventBus;
import io.o2mc.sdk.business.event.EventGenerator;
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
    private boolean usingHttpsEndpoint;

    private final Timer timer = new Timer(); // timer used for dispatching events
    private int dispatchInterval; // interval on which to send events
    private int maxRetries; // max amount of times to retry sending batches

    private DeviceManager deviceManager;
    private EventGenerator eventGenerator;
    private BatchGenerator batchGenerator;
    private EventBus eventBus;

    /**
     * This is central point of communication between the SDK and the app implementing it.
     * The implementing app should never have anything to deal with any other class than this one.
     *
     * @param app      Top-level application class, as defined in the app manifest. Used to automatically detect meta-events like ActivityStarted and ActivityDestroyed.
     * @param endpoint URL to the back-end, defines where to dispatch tracking events to.
     */
    private void shadowConstructor(Application app, String endpoint) {
        setApp(app);
        setEndpoint(endpoint);

        this.deviceManager = new DeviceManager(app);
        this.eventGenerator = new EventGenerator();
        this.batchGenerator = new BatchGenerator();
        this.eventBus = new EventBus();

        BatchDispatcher.getInstance().setO2mc(this);
    }

    /**
     * This is central point of communication between the SDK and the app implementing it.
     * The implementing app should never have anything to deal with any other class than this one.
     *
     * @param app      Top-level application class, as defined in the app manifest. Used to automatically detect meta-events like ActivityStarted and ActivityDestroyed.
     * @param endpoint URL to the back-end, defines where to dispatch tracking events to.
     */
    public O2MC(Application app, String endpoint) {
        shadowConstructor(app, endpoint);
        setDispatchInterval(Config.DEFAULT_DISPATCH_INTERVAL);
        setMaxRetries(Config.DEFAULT_MAX_RETRIES);
    }

    /**
     * This is central point of communication between the SDK and the app implementing it.
     * The implementing app should never have anything to deal with any other class than this one.
     *
     * @param app              Top-level application class, as defined in the app manifest
     * @param endpoint         URL to the back-end, defines where to dispatch tracking events to
     * @param dispatchInterval Tells the EventManager on which intervals it should send the generated events. Denoted in seconds.
     */
    public O2MC(Application app, String endpoint, int dispatchInterval) {
        shadowConstructor(app, endpoint);
        setDispatchInterval(dispatchInterval);
        setMaxRetries(Config.DEFAULT_MAX_RETRIES);
    }

    /**
     * This is central point of communication between the SDK and the app implementing it.
     * The implementing app should never have anything to deal with any other class than this one.
     *
     * @param app              Top-level application class, as defined in the app manifest
     * @param endpoint         URL to the back-end, defines where to dispatch tracking events to
     * @param dispatchInterval Tells the EventManager on which intervals it should send the generated events. Denoted in seconds.
     * @param maxRetries       Sets the max amount of retries for generating batches. Helps to reduce cpu usage / battery draining.
     */
    public O2MC(Application app, String endpoint, int dispatchInterval, int maxRetries) {
        shadowConstructor(app, endpoint);
        setDispatchInterval(dispatchInterval);
        setMaxRetries(maxRetries);
    }

    /**
     * Sets the max amount of retries for generating batches. Helps to reduce cpu usage / battery draining.
     */
    @SuppressWarnings("WeakerAccess") // invalid warning; this method is intended to be used by an App implementing our SDK.
    public void setMaxRetries(int maxRetries) {
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
     * Executed on the Activity lifecycle event 'onActivityCreated' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (BuildConfig.DEBUG) Log.i(TAG, "Activity created.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityStarted' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityStarted(Activity activity) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Activity started.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityStarted' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityResumed(Activity activity) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Activity resumed.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityPaused' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityPaused(Activity activity) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Activity resumed.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityStopped' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityStopped(Activity activity) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Activity stopped.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivitySaveInstanceState' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Activity saved instance state.");
    }

    /**
     * Executed on the Activity lifecycle event 'onActivityDestroyed' of any Activity inside the provided 'App' parameter on initialization of this class.
     */
    @Override
    public void onActivityDestroyed(Activity activity) {
        if (BuildConfig.DEBUG) Log.d(TAG, "Activity destroyed.");
    }

    /**
     * Tracks an event.
     * Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
     *
     * @param eventName name of tracked event
     */
    public void track(String eventName) {
        if (BuildConfig.DEBUG) Log.d(TAG, String.format("Tracked '%s'", eventName));
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
        if (BuildConfig.DEBUG) Log.d(TAG, String.format("Tracked '%s'", eventName));
        Event e = eventGenerator.generateEventWithProperties(eventName, value);
        eventBus.add(e);
    }

    /**
     * Called upon successful HTTP post
     */
    public void dispatchSuccess() {
        if (BuildConfig.DEBUG) Log.d(TAG, "Dispatch successful.");
        reset();
        batchGenerator.lastBatchSucceeded();
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
        if (BuildConfig.DEBUG) Log.d(TAG, "Dispatch failure. Not clearing EventBus.");

        batchGenerator.lastBatchFailed();
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

        timer.schedule(new Dispatcher(), dispatchInterval * 1000, dispatchInterval * 1000);
    }

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

    private void setApp(Application app) {
        if (app == null) {
            if (BuildConfig.DEBUG) Log.w(TAG, "O2MC: Application (context) provided was null. " +
                    "Manually tracked events will still work, however " +
                    "activity lifecycle callbacks will not be automatically detected.");
        } else {
            this.app = app;
            this.app.registerActivityLifecycleCallbacks(this);
        }
    }

    /**
     * Sends all events from the EventBus to the backend, if there are any events.
     */
    class Dispatcher extends TimerTask {
        private static final String TAG = "Dispatcher";

        public void run() {
            // Don't dispatch if we have no events
            if (eventBus.getEvents().size() <= 0) {
                if (BuildConfig.DEBUG)
                    Log.d(TAG, "run: There are no events to dispatch. Skipping.");
                return;
            }

            // Initialize batchGenerator meta data on the first run
            if (batchGenerator.firstRun()) {
                batchGenerator.setDeviceInformation(deviceManager.generateDeviceInformation());
            }

            // Don't try resending a batch if the max retries limit has exceeded
            if (batchGenerator.getRetries() > maxRetries) {
                if (BuildConfig.DEBUG)
                    Log.w(TAG, "run: Max retries limit has been reached. Not trying to resend batch.");
                timer.cancel();
                timer.purge();
                return;
            }

            if (BuildConfig.DEBUG)
                Log.i(TAG, String.format("run: Dispatching batch with '%s' events.", eventBus.getEvents().size()));

            Batch b = batchGenerator.generateBatch(eventBus.getEvents());
            BatchDispatcher.getInstance().post(endpoint, b);
        }
    }
}
