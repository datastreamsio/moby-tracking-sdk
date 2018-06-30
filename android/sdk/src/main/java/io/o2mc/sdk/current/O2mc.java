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

import io.o2mc.sdk.current.business.DeviceManager;
import io.o2mc.sdk.current.business.EventBus;
import io.o2mc.sdk.current.business.EventDispatcher;
import io.o2mc.sdk.current.business.EventGenerator;
import io.o2mc.sdk.current.domain.Event;

/**
 * This is central point of communication between the SDK and the app implementing it.
 * The implementing app should never have anything to deal with any other class than this one.
 */
public class O2mc implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "O2mc";

    private Application app; // required for activity callbacks & context

    private String endpoint;

    private Timer timer = new Timer(); // timer used for dispatching events
    private int dispatchInterval; // interval on which to send events

    private DeviceManager deviceManager;
    private EventGenerator eventGenerator;
    private EventBus eventBus;

    public O2mc(Application app, String endpoint) throws SocketException {
        this.app = app;
        this.app.registerActivityLifecycleCallbacks(this);

        this.endpoint = endpoint;

        this.deviceManager = new DeviceManager(this.app);
        this.eventGenerator = new EventGenerator(deviceManager.generateDeviceInformation()); // todo; optimization; better to do this on another thread. rethink this structure
        this.eventBus = new EventBus();

        EventDispatcher.getInstance().setO2mc(this);
    }

    /**
     * Tells the EventManager on which intervals it should send the generated events.
     *
     * @param seconds time in seconds
     */
    public void setDispatchInterval(int seconds) {
        this.dispatchInterval = seconds;
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

    public void buttonClicked(Button button, @Nullable Object extra) {
        Log.d(TAG, "buttonClicked");
        Event e;
        if (extra == null) {
            e = eventGenerator.generateClickedButtonEvent(button, null);
        } else {
            e = eventGenerator.generateClickedButtonEvent(button, extra);
        }
        if (e == null) {
            Log.e(TAG, "Error in O2mc library. Event should not be null.");
        }
        eventBus.add(e);
    }

    /**
     * Called upon successful HTTP post
     */
    public void dispatchSuccess() {
        // TODO: 29-6-18 keep track of batches
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
        public void run() {
            if (eventBus.getEvents().size() > 0) {
                Log.i(TAG, String.format("run: Dispatching %s events.", eventBus.getEvents().size()));
                EventDispatcher.getInstance().post(endpoint, eventBus.getEvents());
            } else {
                Log.i(TAG, "run: There are no events to dispatch. Skipping dispatch.");
            }
        }
    }
}
