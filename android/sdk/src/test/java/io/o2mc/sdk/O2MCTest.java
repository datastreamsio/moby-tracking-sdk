package io.o2mc.sdk;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class O2MCTest {

  /**
   * Verify that the SDK does not crash on various incorrect input
   */
  @Test
  public void constructor() {
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
      o2mc.track("EventName");
      o2mc.trackWithProperties("EventName", "Props");
      o2mc.reset();
    }
  }
}