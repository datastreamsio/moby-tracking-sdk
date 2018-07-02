package io.o2mc.sdk.current.domain;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

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

    private String type;
    private Object value;

    public Event(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Event{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
