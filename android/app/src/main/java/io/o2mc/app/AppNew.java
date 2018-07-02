package io.o2mc.app;

import android.app.Application;
import android.util.Log;

import io.o2mc.sdk.current.O2MC;

public class AppNew extends Application {

    private static final String TAG = "AppNew";

    private static O2MC o2mc;

    public AppNew() {
        Log.e(TAG, "AppNew: init");
        o2mc = new O2MC(this, "http://10.0.2.2:5000/events");
        o2mc.setDispatchInterval(10);
    }

    public static O2MC getO2mc() {
        return o2mc;
    }
}
