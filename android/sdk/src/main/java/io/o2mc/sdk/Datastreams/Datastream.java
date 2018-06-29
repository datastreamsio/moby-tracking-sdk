package io.o2mc.sdk.Datastreams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import com.jaredrummler.android.device.DeviceName;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.util.ArrayList;

import io.o2mc.sdk.Tracker;

/**
 * Extension on Datastreams & Dimml
 *
 * Root object of the library
 * Initialised by only the application context
 * Has activity state listeners and triggers data tracking
 */
public class Datastream implements Application.ActivityLifecycleCallbacks {
    private Application context;
    private String deviceId = null;
    private Double latitude;
    private Double longitude;
    private LocationManager mLocationManager = null;
    DatastreamsHandler datastreamsHandler;
    private ArrayList<String> hasBeenMapped = new ArrayList<>();
    public final Tracker tracker;

    public Datastream(Application ctx, String endpoint) {
        context = ctx;
        context.registerActivityLifecycleCallbacks(this);
        datastreamsHandler = new DatastreamsHandler(ctx, endpoint);
        tracker = new Tracker(this);
    }

    public Tracker getTracker(){
        return tracker;
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (deviceId == null)
            // TODO; what is this deviceId used for? It's probably better to replace it with advertising ID
            // https://stackoverflow.com/questions/47691310/why-is-using-getstring-to-get-device-identifiers-not-recommended
            deviceId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        // TODO: request location updates.
        if (mLocationManager == null) {
//            && (hasPermission("ACCESS_FINE_LOCATION", activity) || hasPermission("ACCESS_COARSE_LOCATION", activity) )
            mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
//            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, new DatastreamsLocation(this));
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //Add listener here
        datastreamsHandler.update(activity, getGeneralInfo());
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!hasBeenMapped.contains(activity.getClass().getSimpleName()) && getSettings().mapViews()) {
//            new DatastreamsViewmapper(activity);
            hasBeenMapped.add(activity.getClass().getSimpleName());
        }

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        datastreamsHandler.update(activity, getGeneralInfo());
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * Returns a JSONObject with general device info
     * Unique deviceId
     * Phone type
     * connection info
     * operating system
     * ip address
     * location (if enabled)
     * @return
     */
    @SuppressLint("HardwareIds")
    @SuppressWarnings("JavaDoc")
    public JSONObject getGeneralInfo() {
        JSONObject root = new JSONObject();

        try {
            root.put("AppID", context.getPackageName());
            root.put("batch", datastreamsHandler.getDispatcher().getBatchCounter());

            if (getSettings().trackNetworkstate()) {
                root.put("ip", Connectivity.ip());
                root.put("connection", Connectivity.getConnectivityType(context));
            }

            root.put("os", "android");
            root.put("osVersion", Build.VERSION.RELEASE);
            root.put("device", DeviceName.getDeviceName());

            if (getSettings().trackLocation()) {
                root.put("location", new JSONObject().put("latitude", latitude).put("longtitude", longitude));
            }
            if (getSettings().trackUnique()) {
                // TODO; what is this deviceId used for? It's probably better to replace it with advertising ID
                // https://stackoverflow.com/questions/47691310/why-is-using-getstring-to-get-device-identifiers-not-recommended
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                root.put("deviceID", deviceId);
            }
        } catch (JSONException e) {
            Log.e(Datastream.class.getName(), e.getMessage());
        } catch (SocketException e) {
            Log.e(Datastream.class.getName(), e.getMessage());
        }

        return root;
    }

    public boolean hasPermission(String permission, Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(Datastream.class.getName(), e.getMessage());
        }
        return false;
    }

    public DatastreamsHandler getDatastreamsHandler() {
        return datastreamsHandler;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public DatastreamsSettings getSettings() {
        return datastreamsHandler.getSettings();
    }
}