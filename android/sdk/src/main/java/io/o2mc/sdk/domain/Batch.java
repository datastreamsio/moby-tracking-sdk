package io.o2mc.sdk.domain;

import java.util.List;

/**
 * Represents a batch of Events.
 * Essentially contains events and meta data for analytical purposes.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
// Invalid warnings because they're used by gson to be serialized
public class Batch {
    private DeviceInformation deviceInformation;
    private String timestamp;
    private List<Event> events;
    private int number;
    private int retries;

    public Batch(DeviceInformation deviceInformation, String timestamp, List<Event> events, int number) {
        this.deviceInformation = deviceInformation;
        this.timestamp = timestamp;
        this.events = events;
        this.number = number;
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}
