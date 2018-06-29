package io.o2mc.sdk.datastreams;

import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * settings object. Attributes from parsed JSON Object are stored here
 */
public class DatastreamsSettings {
    private boolean override = false;
    private boolean settingsHaveBeenSet = false;
    private boolean trackLocation = false;
    private boolean trackNetworkstate = false;
    private boolean trackUnique = true;
    private boolean onlySendWhenWifi = false;
    private String endpoint;

    private boolean mapViews = false;
    private HashMap<String, DatastreamsActivitySettings> activitySpecificSettings = new HashMap<>();

    public void set(JSONObject settings) {
        if (!settingsHaveBeenSet && !override) {
            try {
                trackLocation = settings.getBoolean("location");
                trackNetworkstate = settings.getBoolean("networkstate");
                trackUnique = settings.getBoolean("unique");
                onlySendWhenWifi = settings.getBoolean("onlySendWhenWifi");
                endpoint = settings.getString("endpoint");
                settingsHaveBeenSet = true;
            } catch (JSONException e) {
                Log.e(DatastreamsSettings.class.getName(), e.getMessage());
            }
        }
    }

    private void override() {
        override = true;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setTrackLocation(Boolean bool) {
        trackLocation = bool;
        override();
    }

    public void setTrackNetworkstate(Boolean bool) {
        trackNetworkstate = bool;
        override();
    }

    /**
     * This setter sets the end point which collects the tracking results.
     * Be sure to use HTTPS in production.
     *
     * @param endpoint HTTP(s) URL where the tracking data should be sent to.
     * @return true on a valid HTTP(s) url, otherwise false.
     */
    public boolean setEndpoint(String endpoint) {
        if (!Patterns.WEB_URL.matcher(endpoint).matches()) {
            Log.w("O2MC_SDK", "Invalid endpoint set. Use a HTTPS or HTTP URL.");
            return false;
        }

        this.endpoint = endpoint;

        if (URLUtil.isHttpUrl(endpoint)) {
            Log.w("O2MC_SDK", "HTTP URL set. Be sure to always use an HTTPS end point in production!");
        }

        return true;
    }

    public boolean mapViews() {
        return mapViews;
    }

    public void setMapViews(boolean mapViews) {
        this.mapViews = mapViews;
    }

    public boolean trackNetworkstate() {
        return trackNetworkstate;
    }

    public boolean trackLocation() {
        return trackLocation;
    }

    public boolean trackUnique() {
        return trackUnique;
    }

    public void setTrackUnique(boolean trackUnique) {
        this.trackUnique = trackUnique;
    }

    public boolean OnlySendWhenWifi() {
        return onlySendWhenWifi;
    }

    public void setOnlySendWhenWifi(boolean onlySendWhenWifi) {
        this.onlySendWhenWifi = onlySendWhenWifi;
    }

    public DatastreamsActivitySettings getDatastreamsActivitySettings(String activity) {
        return activitySpecificSettings.get(activity);
    }

    public void setDatastreamsActivitySettings(String activity, JSONObject obj) {
        activitySpecificSettings.put(activity, new DatastreamsActivitySettings(obj));
    }
}
