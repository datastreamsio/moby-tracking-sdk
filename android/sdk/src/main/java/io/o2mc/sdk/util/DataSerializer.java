package io.o2mc.sdk.util;

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
     * @param generalInfo context info like; appId, OS version, device info, etc.
     */
    public void setGeneralInfo(JSONObject generalInfo) {
        this.generalInfo = generalInfo;
    }

    /**
     * This method is used to add tracking info and combine it with the available generic data.
     * @param param tracking JSON payload.
     * @return combined JSON data.
     */
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
