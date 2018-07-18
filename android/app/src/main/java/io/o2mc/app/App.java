package io.o2mc.app;

import android.app.Application;
import android.util.Log;
import io.o2mc.sdk.O2MC;
import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;

public class App extends Application implements O2MCExceptionListener {

  private static final String TAG = "App";
  private static O2MC o2mc;

  public App() {
    o2mc = new O2MC(null, "http://10.0.2.2:5000/events");
    o2mc.setO2MCExceptionListener(this);
    o2mc.setLogging(false);
  }

  public static O2MC getO2mc() {
    return o2mc;
  }

  @Override public void onO2MCDispatchException(O2MCDispatchException e) {
    Log.e(TAG, "onO2MCDispatchException: Some exception: handling from App.java");
  }

  @Override public void onO2MCDeviceException(O2MCDeviceException e) {
    Log.e(TAG, "onO2MCDeviceException: Some exception: handling from App.java");
  }
}
