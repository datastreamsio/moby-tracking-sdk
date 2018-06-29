package io.o2mc.sdk.Datastreams;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Data tuple used as storage for gathered track data
 */
@SuppressWarnings("WeakerAccess")
public class DataContainer {
    private String elementId;
    private String Activity;
    private String eventType;
    private String viewType;
    private String value;
    private String timestamp;
    private String packagename;
    private String alias;
    private Integer indexWithinActivity;

    public Integer getIndexWithinActivity() {
        return indexWithinActivity;
    }

    public void setIndexWithinActivity(Integer indexWithinActivity) {
        this.indexWithinActivity = indexWithinActivity;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String asString() {
        return asJsonString();
    }

    public String asJsonString() {
        return asJson().toString();
    }

    public JSONObject asJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("alias", getAlias());
            json.put("activity", getActivity());
            json.put("eventType", getEventType());
            json.put("elementValue", getValue());
            json.put("elementID", getElementId());
            json.put("trackingDateTime", getTimestamp());
            json.put("indexWithinActivity", getIndexWithinActivity());
        } catch (JSONException e) {
            Log.e(DataContainer.class.getName(), e.getMessage());
        }
        return json;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @SuppressLint("DefaultLocale")
    // TODO; retrieve the timestamp in a more appropriate/correct manner
    public void setTimestamp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.timestamp = Instant.now().toString();
        }

        this.timestamp = String.format("%tFT%<tTZ", Calendar.getInstance(TimeZone.getTimeZone("Z")));
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
