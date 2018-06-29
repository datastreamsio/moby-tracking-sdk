package io.o2mc.sdk.domain;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.net.SocketException;

/**
 * This is central point of communication between the SDK and the app implementing it.
 * The implementing app should never have anything to deal with any other class than this one.
 */
public class O2mc implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "O2mc";

    private DeviceManager deviceManager;
    private EventManager eventManager;
    private EventGenerator eventGenerator;
    private Application app;

    public O2mc(Application app, String endpoint) {
        this.deviceManager = new DeviceManager(app);
        this.eventManager = new EventManager();
        this.eventGenerator = new EventGenerator();
        app.registerActivityLifecycleCallbacks(this);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
