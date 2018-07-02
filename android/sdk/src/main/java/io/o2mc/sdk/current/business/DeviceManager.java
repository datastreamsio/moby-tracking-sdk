package io.o2mc.sdk.current.business;

import android.app.Application;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.jaredrummler.android.device.DeviceName;

import io.o2mc.sdk.current.domain.DeviceInformation;

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
        DeviceInformation info = new DeviceInformation();

        info.setAppId(app.getPackageName());

        // todo; check if we're allowed to track network state
//        info.setIp(NetworkManager.ip().getCanonicalHostName());
//        info.setConnectionType(NetworkManager.getConnectivityType(context));
        info.setOs("android");
        info.setOsVersion(Build.VERSION.RELEASE);
        info.setDeviceName(DeviceName.getDeviceName());

        // TODO; what is this deviceId used for? It's probably better to replace it with advertising ID
        // https://stackoverflow.com/questions/47691310/why-is-using-getstring-to-get-device-identifiers-not-recommended
        info.setDeviceId(Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID));

        return info;
    }
}
