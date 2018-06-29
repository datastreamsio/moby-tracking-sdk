package io.o2mc.sdk;

import android.app.Application;

import io.o2mc.sdk.datastreams.Datastream;


public class O2MC {
    public final Tracker tracker;

    public O2MC(Application ctx, String endpoint) {
        Datastream datastream = new Datastream(ctx, endpoint);
        tracker = datastream.getTracker();
    }
}
