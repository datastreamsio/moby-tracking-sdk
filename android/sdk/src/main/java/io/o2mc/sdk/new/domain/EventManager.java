package io.o2mc.sdk.domain;

/**
 * Intermediate between events and what to do with them.
 * Manages things like putting new events into the event bus and sending them on a set interval.
 */
public class EventManager {
    private EventGenerator eventGenerator;
    private EventBus eventBus;
}
