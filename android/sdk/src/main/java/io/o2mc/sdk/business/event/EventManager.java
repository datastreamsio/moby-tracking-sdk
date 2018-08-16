package io.o2mc.sdk.business.event;

import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.exceptions.O2MCTrackException;
import io.o2mc.sdk.interfaces.O2MCExceptionNotifier;
import io.o2mc.sdk.util.Util;
import java.util.List;

/**
 * Manages everything that's related to events by making use of a EventBus.
 */
public class EventManager {

  private EventBus eventBus;
  private boolean isStopped;

  // Will be used for future exception handling, once this class gets more complex
  @SuppressWarnings({ "FieldCanBeLocal", "unused" }) private O2MCExceptionNotifier notifier;

  public void init(O2MCExceptionNotifier notifier) {
    this.eventBus = new EventBus();
    this.notifier = notifier;
  }

  /**
   * Generates a new event and adds it to the EventBus.
   *
   * @param eventName name of event to generate
   */
  public void newEvent(String eventName) {
    if (isStopped) return;

    if (!Util.isValidEventName(eventName)) {
      notifier.notifyException(
          new O2MCTrackException(String.format("Event name '%s' is invalid.", eventName)),
          false); // is not fatal for base SDK functionality, next event name may be valid again
      return;
    }

    Event e = eventBus.generateEvent(eventName);
    eventBus.add(e);
  }

  /**
   * Generates a new event with additional properties and adds it to the EventBus.
   *
   * @param eventName name of event to generate
   * @param value value to include in the event
   */
  public void newEventWithProperties(String eventName, Object value) {
    if (isStopped) return;

    if (!Util.isValidEventName(eventName)) {
      notifier.notifyException(
          new O2MCTrackException(String.format("Event name '%s' is invalid.", eventName)),
          false); // is not fatal for base SDK functionality, next event name may be valid again
      return;
    }

    if (!Util.isValidEventValue(value)) {
      notifier.notifyException(
          new O2MCTrackException(String.format("Value '%s' is invalid.", value)),
          false); // is not fatal for base SDK functionality, next event value may be valid again
    }

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
}
