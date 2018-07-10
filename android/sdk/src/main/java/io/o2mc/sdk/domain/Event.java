package io.o2mc.sdk.domain;

@SuppressWarnings({ "FieldCanBeLocal", "unused" })
// Invalid warnings because they're used by gson to be serialized
public class Event {

  private String name;
  private Object value;
  private String timestamp;

  public Event(String name, Object value, String timestamp) {
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

  public String getTimestamp() {
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
