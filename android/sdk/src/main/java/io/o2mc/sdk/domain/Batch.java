package io.o2mc.sdk.domain;

import java.util.List;

/**
 * Represents a batch of Events.
 * Basically contains events and meta data for analytical purposes.
 */
public class Batch {
    private DeviceInformation deviceInformation;
    private String timestamp;
    private List<Event> events;
    private int number;

    public Batch(DeviceInformation deviceInformation, String timestamp, List<Event> events, int number) {
        this.deviceInformation = deviceInformation;
        this.timestamp = timestamp;
        this.events = events;
        this.number = number;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public void setDeviceInformation(DeviceInformation deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
