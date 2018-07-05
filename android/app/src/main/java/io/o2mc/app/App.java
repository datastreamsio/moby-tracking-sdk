package io.o2mc.app;

import android.app.Application;

import io.o2mc.sdk.O2MC;

public class App extends Application {

    private static final String TAG = "App";

    private static O2MC o2mc;

    public App() {
//        o2mc = new O2MC(null, null);
        o2mc = new O2MC(this, "http://10.0.2.2:5000/events");
        o2mc.setDispatchInterval(8);
        o2mc.setMaxRetries(5);
    }

    public static O2MC getO2mc() {
        return o2mc;
    }
}
