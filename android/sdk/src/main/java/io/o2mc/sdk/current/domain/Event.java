package io.o2mc.sdk.current.domain;

public class Event {

    // ============================================================
    // Define event types. This list may be expanded we please.
    // ============================================================
    // ------------------------
    // Naming conventions
    // ------------------------
    // <ACTION>_<NOUN>_<EXTRA>
    // ...where EXTRA means we'd like to submit additional data besides meta data.
    public static final String CLICKED_BUTTON = "CLICKED_BUTTON";
    public static final String CLICKED_BUTTON_EXTRA = "CLICKED_BUTTON_EXTRA";
    public static final String CLICKED_EDITTEXT = "CLICKED_EDITTEXT";
    public static final String CLICKED_EDITTEXT_EXTRA = "CLICKED_EDITTEXT_EXTRA";

    private DeviceInformation deviceInformation;
    private String type;
    private Object value;

    public Event(DeviceInformation deviceInformation, String type, Object value) {
        this.deviceInformation = deviceInformation;
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Event{" +
                "deviceInformation=" + deviceInformation +
                ", type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
