package io.o2mc.sdk.business;

import android.app.Application;
import android.os.Build;
import android.widget.Toast;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;

import static io.o2mc.sdk.util.LogUtil.LogE;

/**
 * Manages all operations specifically targeted to the user's device.
 * Think about retrieving settings, getting device information, Android version, etc.
 */
public class DeviceManager {
  private static final String TAG = "DeviceManager";

  private Application app;
  private O2MCExceptionListener o2MCExceptionListener;

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
      if (o2MCExceptionListener != null) { // if listener is set, inform using an exception
        o2MCExceptionListener.onO2MCDeviceException(new O2MCDeviceException(
            "No device information could be retrieved. Did you supply your Application to the O2MC module?"));
      } else { // no listener set, just log
        LogE(TAG,
            "generateDeviceInformation: No device information could be retrieved. Did you supply your Application to the O2MC module?");
      }
      return null;
    }

    String appId = app.getPackageName();
    String os = "android";
    String osVersion = Build.VERSION.RELEASE;
    String deviceName = android.os.Build.MODEL;

    return new DeviceInformation(
        appId, os, osVersion, deviceName
    );
  }

  public void setO2MCExceptionListener(O2MCExceptionListener o2MCExceptionListener) {
    this.o2MCExceptionListener = o2MCExceptionListener;
  }
}
