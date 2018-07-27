package io.o2mc.sdk.business.event;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SuppressWarnings("RedundantThrows") public class EventManagerTest {

  private EventManager eventManager;

  @Before
  public void setUp() throws Exception {
    eventManager = new EventManager();
    eventManager.init(null);
  }

  @Test
  public void newEvent() {
    // Test a simple case; is an event generated as expected?
    eventManager.newEvent("MyEvent");
    int expected = 1;
    int actual = eventManager.getEvents().size();
    assertEquals(expected, actual);

    // Check the name
    String expectedName = "MyEvent";
    String actualName = eventManager.getEvents().get(0).getName();
    assertEquals(expectedName, actualName);

    // There were no properties given, value should be null
    assertNull(eventManager.getEvents().get(0).getValue());
  }

  @Test
  public void newEventScale() {
    // (Relatively small) scale test; are 100 events generated as expected?
    for (int i = 0; i < 100; i++) {
      eventManager.newEvent(String.format("Event %s", i));
    }

    int expected = 100;
    int actual = eventManager.getEvents().size();

    assertEquals(expected, actual);
  }

  @Test
  public void newEventWithProperties() {
    // Test a simple case; is an event with props generated as expected?
    eventManager.newEventWithProperties("MyEvent", "Prop");

    int expected = 1;
    int actual = eventManager.getEvents().size();

    assertEquals(expected, actual);
  }

  @Test
  public void newEventWithPropertiesScale() {
    // (Relatively small) scale test; are 100 events with props generated as expected?
    for (int i = 0; i < 100; i++) {
      eventManager.newEventWithProperties(String.format("Event with props %s", i), "Prop");
    }
    int expected = 100;
    int actual = eventManager.getEvents().size();
    assertEquals(expected, actual);

    // There were properties given, value should be that
    String expectedValue = "Prop";
    String actualValue = (String) eventManager.getEvents().get(0).getValue();
    assertEquals(expectedValue, actualValue);
  }

  @Test
  public void reset() {
    // Test if the manager returns 0 events after resetting it
    eventManager.newEvent("MyEvent");
    eventManager.reset();
    int expected = 0;
    int actual = eventManager.getEvents().size();
    assertEquals(expected, actual);

    // Test if the manager can still generate events after a reset
    eventManager.newEvent("MyEvent");
    int expectedAfterReset = 1;
    int actualAfterReset = eventManager.getEvents().size();
    assertEquals(expectedAfterReset, actualAfterReset);
  }

  @Test
  public void stop() {
    // Test if everything is deleted on a stop
    eventManager.newEvent("MyEvent");
    eventManager.stop();
    int expected = 0;
    int actual = eventManager.getEvents().size();
    assertEquals(expected, actual);

    // Test if nothing happens after a stop
    eventManager.newEvent("MyEvent");
    int expectedAfterStop = 0;
    int actualAfterStop = eventManager.getEvents().size();
    assertEquals(expectedAfterStop, actualAfterStop);
  }
}