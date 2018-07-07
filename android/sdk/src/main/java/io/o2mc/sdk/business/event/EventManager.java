package io.o2mc.sdk.business.event;

import java.util.List;

import io.o2mc.sdk.domain.Event;

public class EventManager {

    private EventBus eventBus;

    public EventManager() {
        this.eventBus = new EventBus();
    }

    public void newEvent(String eventName) {
        Event e = eventBus.generateEvent(eventName);
        eventBus.add(e);
    }

    public void newEventWithProperties(String eventName, String value) {
        Event e = eventBus.generateEventWithProperties(eventName, value);
        eventBus.add(e);
    }


    /**
     * Removes all tracking events which would otherwise be sent upon next dispatch interval.
     */
    public void reset() {
        eventBus.clearEvents();
    }

    public List<Event> getEventsFromBus() {
        return eventBus.getEvents();
    }

    public void clearEventsFromBus() {
        reset();
    }
}
