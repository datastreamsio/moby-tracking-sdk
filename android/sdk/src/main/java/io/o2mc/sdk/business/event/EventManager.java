package io.o2mc.sdk.business.event;

import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;
import java.util.List;

import static io.o2mc.sdk.util.LogUtil.LogD;

/**
 * Manages everything that's related to events by making use of a EventBus.
 */
public class EventManager {

  private static final String TAG = "EventManager";

  private EventBus eventBus;
  private boolean isStopped;

  // Will be used for future exception handling, once this class gets more complex
  @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private O2MCExceptionListener
      o2MCExceptionListener;

  public EventManager() {
    this.eventBus = new EventBus();
  }

  /**
   * Generates a new event and adds it to the EventBus.
   *
   * @param eventName name of event to generate
   */
  public void newEvent(String eventName) {
    if (isStopped) return;

    Event e = eventBus.generateEvent(eventName);
    eventBus.add(e);
  }

  /**
   * Generates a new event with additional properties and adds it to the EventBus.
   *
   * @param eventName name of event to generate
   * @param value value to include in the event
   */
  public void newEventWithProperties(String eventName, String value) {
    if (isStopped) return;

    LogD(TAG, String.format("Tracked '%s'", eventName));

    Event e = eventBus.generateEventWithProperties(eventName, value);
    eventBus.add(e);
  }

  /**
   * Removes all tracking events which would otherwise be sent upon next dispatch interval.
   */
  public void reset() {
    eventBus.clearEvents();
  }

  /**
   * Returns all known events which are not yet dispatched to the backend
   *
   * @return list of events
   */
  public List<Event> getEvents() {
    return eventBus.getEvents();
  }

  /**
   * When called, disallow generation of events.
   */
  public void stop() {
    reset();
    isStopped = true;
  }

  /**
   * Allow generation of events again.
   */
  public void resume() {
    isStopped = false;
  }

  public void setO2MCExceptionListener(O2MCExceptionListener o2MCExceptionListener) {
    this.o2MCExceptionListener = o2MCExceptionListener;
  }
}
