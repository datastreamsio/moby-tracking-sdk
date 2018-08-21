package io.o2mc.sdk;

import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.exceptions.O2MCEndpointException;
import io.o2mc.sdk.exceptions.O2MCTrackException;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This class checks if the O2MC class does not crash under any circumstance.
 * Functionality of the methods is not verified here, but in the managers / util class(es).
 */
public class O2MCTest {

  private O2MC o2mc;

  @Before
  public void setUp() throws Exception {
    o2mc = new O2MC(null, "http://10.0.2.2:5000/events");
  }

  @After
  public void tearDown() throws Exception {
    o2mc.stop();
  }

  /**
   * All public methods are individually tested in this class, but one method call should not
   * influence other functionality. This method will execute various methods to verify whether
   * or not the SDK will crash after performing another action prior to calling this method.
   *
   * @param o2mc o2mc instance which has been used before
   */
  private void executeAllMethods(O2MC o2mc) {
    o2mc.setMaxRetries(0);
    o2mc.setO2MCExceptionListener(null);
    o2mc.setIdentifier(null);
    o2mc.setSessionIdentifier();
    o2mc.setLogging(false);
    o2mc.track(null);
    o2mc.trackWithProperties(null, null);
    o2mc.stop();
    o2mc.resume();
  }

  @Test
  public void constructors() {
    // Check endpoint input
    O2MC o2mc;
    List<String> endpoints = new ArrayList<>();
    endpoints.add("something");
    endpoints.add(null);
    endpoints.add("htt://10.020.0/");
    endpoints.add("0");
    endpoints.add("https://www.google.com/");

    // Create a new instance of O2MC for every endpoint. Verify that the SDK does
    // not crash when executing some methods afterwards.
    for (String e : endpoints) {
      o2mc = new O2MC(null, e);
      executeAllMethods(o2mc);
    }
  }

  @Test
  public void setMaxRetries() {
    o2mc.setMaxRetries(-10000);
    executeAllMethods(o2mc);

    o2mc.setMaxRetries(-1);
    o2mc.setMaxRetries(0);
    o2mc.setMaxRetries(1);
    o2mc.setMaxRetries(10);
    o2mc.setMaxRetries(10000);
    executeAllMethods(o2mc);
  }

  @Test
  public void setO2MCExceptionListener() {
    o2mc.setO2MCExceptionListener(null);
    executeAllMethods(o2mc);

    o2mc.setO2MCExceptionListener(new O2MCExceptionListener() {
      @Override public void onO2MCDispatchException(O2MCDispatchException e) {
      }

      @Override public void onO2MCDeviceException(O2MCDeviceException e) {
      }

      @Override public void onO2MCEndpointException(O2MCEndpointException e) {
      }

      @Override public void onO2MCTrackException(O2MCTrackException e) {

      }
    });
    executeAllMethods(o2mc);
  }

  @Test
  public void setIdentifier() {
    o2mc.setIdentifier(null);
    executeAllMethods(o2mc);

    o2mc.setIdentifier("something");
    o2mc.setIdentifier("1");
    o2mc.setIdentifier("something a bit longer");
    o2mc.setIdentifier("something-a-bit-longer");
    executeAllMethods(o2mc);
  }

  @Test
  public void setSessionIdentifier() {
    o2mc.setSessionIdentifier();
    executeAllMethods(o2mc);
  }

  @Test
  public void setLogging() {
    o2mc.setLogging(true);
    executeAllMethods(o2mc);

    o2mc.setLogging(false);
    executeAllMethods(o2mc);
  }

  @Test
  public void track() {
    o2mc.track(null);
    executeAllMethods(o2mc);

    o2mc.track("something");
    executeAllMethods(o2mc);
  }

  @Test
  public void trackWithProperties() {
    o2mc.trackWithProperties(null, null);
    executeAllMethods(o2mc);

    o2mc.trackWithProperties("something", null);
    executeAllMethods(o2mc);

    o2mc.trackWithProperties(null, null);
    executeAllMethods(o2mc);

    o2mc.trackWithProperties(null, "something");
    executeAllMethods(o2mc);
  }

  @Test
  public void stop() {
    o2mc.stop();
    executeAllMethods(o2mc);
  }

  @Test
  public void resume() {
    o2mc.resume();
    executeAllMethods(o2mc);
  }
}