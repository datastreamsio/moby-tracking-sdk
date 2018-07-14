package io.o2mc.sdk.business.event;

import io.o2mc.sdk.domain.Event;
import java.util.List;

/**
 * Manages everything that's related to events by making use of a EventBus.
 */
public class EventManager {

  private EventBus eventBus;
  private boolean isStopped;

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
   * When called, no more events will be generated
   */
  public void stop() {
    reset();
    isStopped = true;
  }
}
