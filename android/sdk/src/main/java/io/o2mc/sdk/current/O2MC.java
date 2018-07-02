package io.o2mc.sdk.current;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;

import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import io.o2mc.sdk.current.business.BatchGenerator;
import io.o2mc.sdk.current.business.DeviceManager;
import io.o2mc.sdk.current.business.EventBus;
import io.o2mc.sdk.current.business.EventDispatcher;
import io.o2mc.sdk.current.business.EventGenerator;
import io.o2mc.sdk.current.business.Util;
import io.o2mc.sdk.current.domain.Batch;
import io.o2mc.sdk.current.domain.Event;

/**
 * This is central point of communication between the SDK and the app implementing it.
 * The implementing app should never have anything to deal with any other class than this one.
 */
public class O2MC implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "O2MC";

    private Application app; // required for activity callbacks & context

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
            Log.e(TAG, String.format("O2MC: Dispatch interval '%s' is invalid. Note that the value must be positive and is denoted in seconds.\nNot dispatching events.", dispatchInterval));
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.i(TAG, "Activity created.");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "Activity started.");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "Activity resumed.");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.i(TAG, "Activity resumed.");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.i(TAG, "Activity stopped.");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.i(TAG, "Activity saved instance state.");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.i(TAG, "Activity stopped.");
    }

    public void track(String eventName) {
        Log.d(TAG, String.format("Tracked '%s'", eventName));
        Event e = eventGenerator.generateEvent(eventName);
        eventBus.add(e);
    }

    public void trackWithProperties(String eventName, String propertiesAsJson) {
        Log.d(TAG, String.format("Tracked '%s'", eventName));
        Event e = eventGenerator.generateEventWithProperties(eventName, propertiesAsJson);
        eventBus.add(e);
    }

    /**
     * Called upon successful HTTP post
     */
    public void dispatchSuccess() {
        Log.d(TAG, "Dispatch successful.");
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
    public void startDispatching() {
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
                Log.i(TAG, "run: There are no events to dispatch. Skipping.");
                return;
            }

            // Initialize batchGenerator meta data on the first run
            if (batchGenerator.firstRun()) {
                batchGenerator.setDeviceInformation(deviceManager.generateDeviceInformation());// todo; optimization; better to do this on another thread. rethink this structure
            }

            Log.i(TAG, String.format("run: Dispatching batch with '%s' events.", eventBus.getEvents().size()));
            Batch b = batchGenerator.generateBatch(eventBus.getEvents());
            EventDispatcher.getInstance().post(endpoint, b);
        }
    }
}
