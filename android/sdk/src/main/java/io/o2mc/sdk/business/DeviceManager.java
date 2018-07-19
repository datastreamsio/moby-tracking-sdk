package io.o2mc.sdk.business;

import android.app.Application;
import android.os.Build;
import android.provider.Settings;
import com.jaredrummler.android.device.DeviceName;
import io.o2mc.sdk.domain.DeviceInformation;

import static io.o2mc.sdk.util.LogUtil.LogE;

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
      LogE(TAG,
          "generateDeviceInformation: No device information could be retrieved. Did you supply your Application to the O2MC module?");
      return null;
    }

    String appId = app.getPackageName();
    String os = "android";
    String osVersion = Build.VERSION.RELEASE;
    String deviceName = DeviceName.getDeviceName();

    return new DeviceInformation(
        appId, os, osVersion, deviceName
    );
  }
}
