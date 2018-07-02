package io.o2mc.app;

import android.app.Application;

import io.o2mc.sdk.old.O2MC;

public class AppOld extends Application {

    private static O2MC o2mc;
    private static Application instance;

    public AppOld() {
        /**
         * Initialising the Datastreams module
         */
        o2mc = new O2MC(this, "http://10.0.2.2:5000/events");
        o2mc.tracker.setDispatchInterval(10);
    }

    public static Application getContext() {
        return instance;
    }

    public static O2MC getO2mc() {
        return o2mc;
    }
}
