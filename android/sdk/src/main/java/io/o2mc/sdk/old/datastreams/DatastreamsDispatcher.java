package io.o2mc.sdk.old.datastreams;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;

import io.o2mc.sdk.old.util.DataPoster;
import io.o2mc.sdk.old.util.DataSerializer;

/**
 * This class handles the dispatching of data
 * All gathered tracking data passes through one of the two dispatch methods
 */
public class DatastreamsDispatcher {
    private Activity context;
    private DatastreamsSettings settings;

    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    // TODO; remove suppress warning; actually use the items or refactor/remove them
    private DatastreamsHandler datastreamsHandler;
    private int batchCounter = 0;
    private boolean postInProgress;
    private ArrayList<JSONObject> dataContainers = new ArrayList<>();
    private JSONObject generalInfo;
    private int dispatchThreshold = 5;

    public DatastreamsDispatcher(DatastreamsHandler datastreamsHandler) {
        this.settings = datastreamsHandler.getSettings();
        this.datastreamsHandler = datastreamsHandler;

        // Set dispatcher to trigger http success callbacks.
        DataPoster.getInstance().setDatastreamsDispatcher(this);
    }

    public void update(Activity activity, JSONObject generalInfo) {
        context = activity;
        this.generalInfo = generalInfo;
    }

    /**
     * Used to set dispatchThreshold, when x datacontainers have been gathered send data to endpoint
     *
     * @param threshold int - default 5.
     */
    public void setDispatchThreshold(int threshold) {
        this.dispatchThreshold = threshold;
    }

    @SuppressWarnings("unchecked")
    // Ignore because there's an if statement which does check the amount of items to serialize
    public void dispatch(DataContainer dataContainer) {
        dataContainers.add(dataContainer.asJson());

        String endpoint = getSettings().getEndpoint();

        if (dataContainers.size() > dispatchThreshold) {
            if (!postInProgress) {
                if (settings.OnlySendWhenWifi()) {
                    if (Connectivity.getConnectivityType(context).toUpperCase().equals("WIFI")) {
                        DataSerializer serializer = new DataSerializer();
                        serializer.setGeneralInfo(generalInfo);
                        JSONObject root = serializer.serialize(dataContainers);
                        DataPoster.getInstance().post(endpoint, root.toString());
                        postInProgress = true;
                    }
                } else {
                    DataSerializer serializer = new DataSerializer();
                    serializer.setGeneralInfo(generalInfo);
                    JSONObject root = serializer.serialize(dataContainers);
                    DataPoster.getInstance().post(endpoint, root.toString());
                    postInProgress = true;
                }

            }
        }

        Log.d("DATA", dataContainer.asString());
    }

    /**
     * This method is derived from the void Dispatch() method but instead of adhering to a threshold it dispatches
     * everything it has gathered up until now.
     *
     * @param generalInfo as JSONObject
     */
    @SuppressWarnings("unchecked")
    // Ignore because there's an if statement which does check the amount of items to serialize
    public void dispatchNow(JSONObject generalInfo) {
        String endpoint = getSettings().getEndpoint();

        if (!postInProgress && dataContainers.size() > 0) {
            if (settings.OnlySendWhenWifi()) {
                if (Connectivity.getConnectivityType(context).toUpperCase().equals("WIFI")) {
                    DataSerializer serializer = new DataSerializer();
                    serializer.setGeneralInfo(generalInfo);
                    JSONObject root = serializer.serialize(dataContainers);
                    DataPoster.getInstance().post(endpoint, root.toString());
                    postInProgress = true;
                }
            } else {
                DataSerializer serializer = new DataSerializer();
                serializer.setGeneralInfo(generalInfo);
                JSONObject root = serializer.serialize(dataContainers);
                DataPoster.getInstance().post(endpoint, root.toString());
                postInProgress = true;
            }

        }
    }

    @SuppressWarnings("WeakerAccess")
    public DatastreamsSettings getSettings() {
        return settings;
    }

    public int getBatchCounter() {
        return batchCounter;
    }

    public void callback() {
        dataContainers.clear();
        batchCounter += 1;
        postInProgress = false;
    }

    public void callbackFailure() {
        postInProgress = false;
    }
}
