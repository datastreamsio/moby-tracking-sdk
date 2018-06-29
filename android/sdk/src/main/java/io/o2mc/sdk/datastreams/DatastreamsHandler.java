package io.o2mc.sdk.datastreams;

import android.app.Activity;
import android.app.Application;

import org.json.JSONObject;

/**
 * This class handles injecting click listeners as middleware
 * and tracks views when they are loaded into memory
 */
public class DatastreamsHandler {

    private DatastreamsSettings datastreamsSettings;
    private DatastreamsDispatcher datastreamsDispatcher;

    public DatastreamsHandler(Application context, String endpoint) {
        datastreamsSettings = new DatastreamsSettings();
        datastreamsSettings.setEndpoint(endpoint);

        datastreamsDispatcher = new DatastreamsDispatcher(this);
    }

    public DatastreamsDispatcher getDispatcher() {
        return datastreamsDispatcher;
    }

    public DatastreamsSettings getSettings() {
        return datastreamsSettings;
    }

    public void update(Activity activity, JSONObject generalInfo) {
        datastreamsDispatcher.update(activity, generalInfo);
    }
}