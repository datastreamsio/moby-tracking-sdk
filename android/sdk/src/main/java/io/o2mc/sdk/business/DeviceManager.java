package io.o2mc.sdk.business;

import android.app.Application;
import android.os.Build;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.interfaces.O2MCExceptionNotifier;

/**
 * Manages all operations specifically targeted to the user's device.
 * Think about retrieving settings, getting device information, Android version, etc.
 */
public class DeviceManager {

  private Application app;

  private O2MCExceptionNotifier notifier;

  public DeviceManager(O2MCExceptionNotifier notifier, Application app) {
    this.notifier = notifier;
    this.app = app;
  }

  /**
   * Generates an object containing device information.
   *
   * @return information about a phone
   */
  public DeviceInformation generateDeviceInformation() {
    if (app == null) {
      notifier.notifyException(new O2MCDeviceException(
          "No device information could be retrieved. Did you supply your Application to the O2MC module?"));
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
}
