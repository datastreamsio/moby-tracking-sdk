package io.o2mc.sdk.business;

import android.app.Application;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.jaredrummler.android.device.DeviceName;

import io.o2mc.sdk.BuildConfig;
import io.o2mc.sdk.domain.DeviceInformation;

/**
 * Manages all operations specifically targeted to the user's device.
 * Think about retrieving settings, getting device information, Android version, etc.
 */
public class DeviceManager {
    private static final String TAG = "DeviceManager";

    private Application app;

    public DeviceManager(Application app) {
        this.app = app;
    }

    /**
     * Generates an object containing device information.
     *
     * @return information about a phone
     */
    public DeviceInformation generateDeviceInformation() {
        if (app == null) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "generateDeviceInformation: No device information could be retrieved. Did you supply your Application to the O2MC module?");
            return null;
        }

        DeviceInformation info = new DeviceInformation();

        info.setAppId(app.getPackageName());
        info.setOs("android");
        info.setOsVersion(Build.VERSION.RELEASE);
        info.setDeviceName(DeviceName.getDeviceName());

        return info;
    }
}
