package io.o2mc.sdk;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import io.o2mc.sdk.Datastreams.DataContainer;
import io.o2mc.sdk.Datastreams.Datastream;

/**
 * App tagging class for dispatching key->value pairs to set endpoint
 * Methods from this class can be used from throughout your android app
 */
public class Tracker {
    private String trackingId;
    private String alias = "";
    private String identity = "";
    private boolean timerHasStarted = false;
    private HashMap<String, ArrayList<JSONObject>> funnel = new HashMap<>();
    private final Datastream ds;
    private Timer timer = new Timer();

    public Tracker(Datastream datastream) {
        ds = datastream;
        try {
            UUID uuid =  UUID.randomUUID();
            addToFunnel("alias", new JSONObject().put("alias", uuid).put("identity", this.identity));
            alias = uuid.toString();
        } catch (JSONException e) {
            Log.e(Tracker.class.getName(), e.getMessage());
        }
    }

    private void addToFunnel(String key, JSONObject props) {
        if (funnel.get(key) == null) funnel.put(key, new ArrayList<JSONObject>());
        funnel.get(key).add(props);
        Log.i("Added to funnel", key+": "+props.toString());
    }

    private void updateFunnel(String key, int index, JSONObject props) {
        funnel.get(key).set(index, props);
        Log.i("Updated in funnel", key + ": " + props.toString());
    }


    public void track(String eventName) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("event", eventName);
            obj.put("alias", alias);
            obj.put("identity", identity);
            obj.put("time", new Timestamp(getTimestamp()));
            addToFunnel(eventName, obj);
        } catch (JSONException e) {
            Log.e(Tracker.class.getName(), e.getMessage());
        }
    }

    public void trackWithProperties(String eventName, String propertiesAsJson) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("event", eventName);
            obj.put("alias", alias);
            obj.put("identity", identity);
            obj.put("time", new Timestamp(getTimestamp()));
            obj.put("properties", propertiesAsJson);
            addToFunnel(eventName, obj);
        } catch (JSONException e) {
            Log.e(Tracker.class.getName(), e.getMessage());
        }
    }

    public void createAlias(String alias) {
        this.alias = alias;
        JSONObject obj = new JSONObject();
        try {
            obj.put("event", "alias");
            obj.put("identity", identity);
            obj.put("time", new Timestamp(getTimestamp()));
            obj.put("properties", alias);
            addToFunnel("alias", obj);
        } catch (JSONException e) {
            Log.e(Tracker.class.getName(), e.getMessage());
        }
    }

    public void identify(String identifier) {
        try {
            identity = identifier;
            addToFunnel("identity", new JSONObject().put("identity", identifier).put("alias", this.alias));
//            updateFunnel("identity", 0, new JSONObject().put("identity", identifier).put("alias", this.alias));
        } catch (JSONException e) {
            Log.e(Tracker.class.getName(), e.getMessage());
        }
    }

    private String timedEvent = "";
    private String timedEventProperties = "";
    private Long startTime = 0L;

    public void timeEventStart(String eventName) {
        startTime = getTimestamp();
        timedEvent = eventName;
    }

    private Long stopTime = 0L;

    public void timeEventStop(String eventName) {
        if (eventName.equals(timedEvent)) {
            stopTime = getTimestamp();
            try {
                addToFunnel(timedEvent, new JSONObject().put("start", startTime).put("stop", stopTime).put("identity",identity).put("alias",alias));
            } catch (JSONException e) {
                Log.e(Tracker.class.getName(), e.getMessage());
            }
        }
    }

    public void timeEventStart(String eventName, String propertiesAsJson) {
        startTime = getTimestamp();
        timedEvent = eventName;
        timedEventProperties = propertiesAsJson;
    }

    public void timeEventStop(String eventName, String propertiesAsJson) {
        if (eventName.equals(timedEvent)) {
            stopTime = getTimestamp();
            try {
                JSONObject obj = new JSONObject().put("stopProperties", propertiesAsJson);
                obj.put("alias", alias);
                obj.put("identity", identity);
                obj.put("startProperties", timedEventProperties);
                addToFunnel(timedEvent, obj.put("start", startTime).put("stop", stopTime));
            } catch (JSONException e) {
                Log.e(Tracker.class.getName(), e.getMessage());
            }
        }
    }

    public void trackCharge(double amount, String propertiesAsJson) {

    }


    public void reset() {
        ds.getDatastreamsHandler().getDispatcher().callback();
        funnel.clear();
    }

    public void setEndpoint(String endpoint) {
        ds.getSettings().setEndpoint(endpoint);
    }

    public void setDispatchInterval(int interval) {
        try {
            if(timerHasStarted) {
                timer.cancel();
                timer.purge();
            }
            timer.schedule(new Dispatcher(), interval * 1000, interval * 1000);
        } catch(IllegalStateException e){
            Log.e("DISPATCHTIMER", e.getMessage());
        }
    }

    private Long getTimestamp() {
        java.util.Date date = new java.util.Date();
        return date.getTime();
    }

    class Dispatcher extends TimerTask {
        public void run() {
            Iterator it = funnel.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pair = (Map.Entry) it.next();

                for (JSONObject obj : funnel.get(pair.getKey())) {
                    DataContainer c = new DataContainer();
                    c.setEventType((String) pair.getKey());
                    c.setValue(obj.toString());
                    c.setTimestamp();
                    ds.getDatastreamsHandler().getDispatcher().dispatch(c);
                }
//                System.out.println(pair.getKey() + " = " + pair.getValue());
                it.remove(); // avoids a ConcurrentModificationException
            }
            ds.getDatastreamsHandler().getDispatcher().dispatchNow(ds.getGeneralInfo());
            funnel.clear();
        }
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}