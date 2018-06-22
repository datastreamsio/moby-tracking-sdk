package io.o2mc.sdk.Datastreams;

import android.app.Activity;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.o2mc.sdk.util.DataPoster;
import io.o2mc.sdk.util.DataSerializer;

/**
 * This class handles the dispatching of data
 * All gathered tracking data passes through one of the two dispatch methods
 */
public class DatastreamsDispatcher {
    private Activity context;
    private DatastreamsSettings settings;
    private DatastreamsHandler datastreamsHandler;
    private int batchCounter = 0;
    public boolean postInProgress;
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
     * @param threshold
     */
    public void setDispatchThreshold(int threshold) {
        this.dispatchThreshold = threshold;
    }

    public void dispatch(DataContainer dataContainer) {
        dataContainers.add(dataContainer.asJson());

        String endpoint = getSettings().getEndpoint();

        if (dataContainers.size() > dispatchThreshold) {
            if (!postInProgress) {
                if (settings.OnlySendWhenWifi()) {
                    if (Connectivity.getConnectivityType(context).toUpperCase().equals("WIFI")) {
                        try {
                            DataSerializer serializer = new DataSerializer();
                            serializer.setGeneralInfo(generalInfo);
                            JSONObject root = serializer.serialize(dataContainers);
                            DataPoster.getInstance().post(endpoint, root.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        postInProgress = true;
                    }
                } else {
                    try {
                        DataSerializer serializer = new DataSerializer();
                        serializer.setGeneralInfo(generalInfo);
                        JSONObject root = serializer.serialize(dataContainers);
                        DataPoster.getInstance().post(endpoint, root.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    postInProgress = true;
                }

            }
        }

        Log.e("DATA", dataContainer.asString());
    }

    /**
     * This method is derived from the void Dispatch() method but instead of adhering to a threshold it dispatches
     * everything it has gathered up until now.
     * @param generalInfo
     */
    public void dispatchNow(JSONObject generalInfo) {
        String endpoint = getSettings().getEndpoint();

        if (!postInProgress && dataContainers.size() > 0) {
            if (settings.OnlySendWhenWifi()) {
                if (Connectivity.getConnectivityType(context).toUpperCase().equals("WIFI")) {
                    try {
                        DataSerializer serializer = new DataSerializer();
                        serializer.setGeneralInfo(generalInfo);
                        JSONObject root = serializer.serialize(dataContainers);
                        DataPoster.getInstance().post(endpoint, root.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    postInProgress = true;
                }
            } else {
                try {
                    DataSerializer serializer = new DataSerializer();
                    serializer.setGeneralInfo(generalInfo);
                    JSONObject root = serializer.serialize(dataContainers);
                    DataPoster.getInstance().post(endpoint, root.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                postInProgress = true;
            }

        }
    }


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
