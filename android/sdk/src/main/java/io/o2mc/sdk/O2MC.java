package io.o2mc.sdk;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

/**
 * This is central point of communication between the SDK and the app implementing it.
 * The implementing app should never have anything to deal with any other class than this one.
 */
public class O2MC implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "O2MC";

    @SuppressWarnings("FieldCanBeLocal")
    // keeping the variable here may prevent GC from removing the reference to the App
    private Application app; // required for activity callbacks & context (may need it for more callbacks in the future)

    private TrackingManager trackingManager;

    /**
     * This is central point of communication between the SDK and the app implementing it.
     * The implementing app should never have anything to deal with any other class than this one.
     *
     * @param app      Top-level application class, as defined in the app manifest. Used to automatically detect meta-events like ActivityStarted and ActivityDestroyed.
     * @param endpoint URL to the back-end, defines where to dispatch tracking events to.
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // potentially used by App implementing our SDK
    public O2MC(Application app, String endpoint) {
        this(app, endpoint, Config.DEFAULT_DISPATCH_INTERVAL, Config.DEFAULT_MAX_RETRIES);
    }

    /**
     * This is central point of communication between the SDK and the app implementing it.
     * The implementing app should never have anything to deal with any other class than this one.
     *
     * @param app              Top-level application class, as defined in the app manifest
     * @param endpoint         URL to the back-end, defines where to dispatch tracking events to
     * @param dispatchInterval Tells the EventManager on which intervals it should send the generated events. Denoted in seconds.
     */
    @SuppressWarnings({"unused", "WeakerAccess"}) // potentially used by App implementing our SDK
    public O2MC(Application app, String endpoint, int dispatchInterval) {
        this(app, endpoint, dispatchInterval, Config.DEFAULT_MAX_RETRIES);
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
    @SuppressWarnings({"unused", "WeakerAccess"}) // potentially used by App implementing our SDK
    public O2MC(Application app, String endpoint, int dispatchInterval, int maxRetries) {
        if (app == null) {
            if (BuildConfig.DEBUG) Log.w(TAG, "O2MC: Application (context) provided was null. " +
                    "Manually tracked events will still work, however " +
                    "activity lifecycle callbacks will not be automatically detected.");
        } else {
            this.app = app;
            this.app.registerActivityLifecycleCallbacks(this);
        }

        trackingManager = new TrackingManager(app, endpoint, dispatchInterval, maxRetries);
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
        trackingManager.track(eventName);
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
        trackingManager.trackWithProperties(eventName, value);
    }

    public void reset() {
        if (BuildConfig.DEBUG) Log.d(TAG, "Reset all events & batches.");
        trackingManager.reset();
    }
}
