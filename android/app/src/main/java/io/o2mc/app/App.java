package io.o2mc.app;

import android.app.Application;
import android.util.Log;
import io.o2mc.sdk.O2MC;
import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.exceptions.O2MCEndpointException;
import io.o2mc.sdk.exceptions.O2MCTrackException;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;

public class App extends Application {

  private static O2MC o2mc;

  public App() {
    o2mc = new O2MC(this, "http://10.0.2.2:5000/events");
  }

  public static O2MC getO2mc() {
    return o2mc;
  }
}
