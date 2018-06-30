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

    public void dispatchSuccess() {
        // TODO: 29-6-18 clear databus
        // TODO: 29-6-18 keep track of batches
        Log.d(TAG, "Dispatch succeeded.");
//        dataContainers.clear();
//        batchCounter += 1;
//        postInProgress = false;
    }

    public void dispatchFailure() {
        Log.d(TAG, "Dispatch failed.");
    }

    public void startDispatching() {
        timer.schedule(new Dispatcher(), dispatchInterval * 1000, dispatchInterval * 1000);
    }

    class Dispatcher extends TimerTask {
        public void run() {
            // TODO: 30-6-18 dispatch events from EventBus now
//            for (Map.Entry<String, ArrayList<JSONObject>> entry : funnel.entrySet()) {
//                for (JSONObject obj : entry.getValue()) {
//                    DataContainer c = new DataContainer();
//                    c.setEventType(entry.getKey());
//                    c.setValue(obj.toString());
//                    c.setTimestamp();
//                    ds.getDatastreamsHandler().getDispatcher().dispatch(c);
//                }
//            }
//            ds.getDatastreamsHandler().getDispatcher().dispatchNow(ds.getGeneralInfo());
//            funnel.clear();
            Log.i(TAG, "Dispatching events...");
            EventDispatcher.getInstance().post(endpoint, eventBus.getEvents());
        }
    }
}
