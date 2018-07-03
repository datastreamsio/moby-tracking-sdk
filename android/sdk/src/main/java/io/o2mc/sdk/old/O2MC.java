package io.o2mc.sdk.old;

import android.app.Application;

import io.o2mc.sdk.old.datastreams.Datastream;


public class O2MC {
    private static final String TAG = "O2MC";
    private Application context;
    private Datastream datastream;
    public final Tracker tracker;

    public O2MC(Application ctx, String endpoint) {
        context = ctx;
        datastream = new Datastream(ctx, endpoint);
        tracker = datastream.getTracker();
    }
}
