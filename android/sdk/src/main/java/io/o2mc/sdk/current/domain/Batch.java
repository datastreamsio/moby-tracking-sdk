package io.o2mc.sdk.current.domain;

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a batch of Events.
 * Basically contains events and meta data for analytical purposes.
 */
public class Batch {
    private DeviceInformation deviceInformation;
    private Timestamp timestamp;
    private List<Event> events;

    public Batch(DeviceInformation deviceInformation, Timestamp timestamp, List<Event> events) {
        this.deviceInformation = deviceInformation;
        this.timestamp = timestamp;
        this.events = events;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
