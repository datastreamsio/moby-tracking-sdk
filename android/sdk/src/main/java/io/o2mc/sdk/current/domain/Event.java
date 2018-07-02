package io.o2mc.sdk.current.domain;

import java.sql.Timestamp;

public class Event {

    private String name;
    private Object value;
    private Timestamp timestamp;

    public Event(String name, Object value, Timestamp timestamp) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }
}
