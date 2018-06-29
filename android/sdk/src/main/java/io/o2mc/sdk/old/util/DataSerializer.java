package io.o2mc.sdk.old.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class serializes and structures given JSON as expected by the tracking API.
 */
public class DataSerializer {
    private JSONObject generalInfo;

    public DataSerializer() {
        super();
    }

    /**
     * Sets general context info.
     *
     * @param generalInfo context info like; appId, OS version, device info, etc.
     */
    public void setGeneralInfo(JSONObject generalInfo) {
        this.generalInfo = generalInfo;
    }

    /**
     * This method is used to add tracking info and combine it with the available generic data.
     *
     * @param param tracking JSON payload.
     * @return combined JSON data.
     */
    @SuppressWarnings("unchecked")
    // Ignore the warning for now; I intend to refactor serializing and adding items
    // to a JSONObject manually to using domain objects in combination with GSON
    public JSONObject serialize(ArrayList<JSONObject>... param) {
        JSONArray dataAsJson = new JSONArray(param[0]);

        JSONObject root = new JSONObject();

        try {
            root.put("tracked", dataAsJson);
            root.put("application", generalInfo);
        } catch (JSONException e) {
            Log.e(DataSerializer.class.getName(), e.getMessage());
        }

        return root;
    }
}
