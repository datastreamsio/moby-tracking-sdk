package io.o2mc.sdk.Datastreams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Class which I added when I wanted to implement activity specific settings
 * This has now been set on _COULD_
 * it is not currently being used
 */
public class DatastreamsActivitySettings {

    public static final String DISPATCH_ON_EXIT  = "dispatchOnExit";

    private HashMap<String, Boolean> settingsMap = new HashMap<>();

    public DatastreamsActivitySettings(JSONObject settings) {
        Iterator<?> keys = settings.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                settingsMap.put(key, (Boolean) settings.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean getSetting(String key){
        return settingsMap.get(key);
    }
}
