package io.o2mc.sdk.current.business;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import io.o2mc.sdk.current.O2mc;
import io.o2mc.sdk.old.datastreams.DatastreamsDispatcher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import io.o2mc.sdk.current.domain.Event;

/**
 * Dispatches events in JSON format.
 * Singleton class, guaranteed to have only one instance in the apps lifecycle.
 * Sends events in a thread-safe manner.
 */
public class EventDispatcher {

    private static final String TAG = "EventDispatcher";

    private static Gson gson;
    private static O2mc o2mc;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(false).build();

    // ==========================================
    // region Start singleton technicalities
    // ==========================================
    private static EventDispatcher mInstance;
    private static EventDispatcher eventDispatcher;

    private EventDispatcher() {
    }

    public static EventDispatcher getInstance() {
        if (mInstance != null) {
            return mInstance;
        }

        synchronized (EventDispatcher.class) {
            if (mInstance == null) {
                mInstance = new EventDispatcher();
                gson = new Gson();
            }
        }

        return mInstance;
    }
    // ==========================================
    // endregion Start singleton technicalities
    // ==========================================

    /**
     * Sends analytical events to backend specified by URL in JSON format.
     *
     * @param url    url of backend to send events to
     * @param events list of Event objects
     */
    public void post(String url, List<Event> events) {
        try {
            String json = eventsToJson(events);
            Log.d(TAG, String.format("About to post: %s", json));
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    EventDispatcher.getInstance().failureCallback();
                    Log.e(TAG, String.format("Unable to post data: '%s'", e.getMessage()));
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // Http response indicates success, inform user and SDK
                        EventDispatcher.getInstance().successCallback();
                        if (response.body() == null) {
                            Log.w(TAG, "onResponse: empty http response from backend");
                        } else {
                            Log.i(TAG, String.format("onResponse: http response was '%s'", response.body().string()));
                        }
                    } else {
                        // Http response indicates failure, inform user and SDK
                        EventDispatcher.getInstance().failureCallback();
                        Log.w(TAG, String.format("onResponse: Http response indicates failure: '%s'", response.body().string()));
                    }
                }
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            EventDispatcher.getInstance().failureCallback();
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Transforms a list of events to an array of JsonObjects in Json format
     *
     * @param events list of events
     * @return list of JsonObjects in JSON format, as String
     */
    private String eventsToJson(List<Event> events) {
        List<JsonObject> result = new ArrayList<>();
        for (Event e : events) {
            result.add((JsonObject) gson.toJsonTree(e));
        }
        return gson.toJson(result);
    }

    public void setO2mc(O2mc o2mc) {
        EventDispatcher.o2mc = o2mc;
        Log.d(TAG, "Set o2mc field.");
    }

    /**
     * Called upon successful HTTP post
     */
    private void successCallback() {
        if (o2mc != null) {
            o2mc.dispatchSuccess();
        } else {
            Log.d(TAG, "O2mc variable is null.");
        }
    }

    /**
     * Called upon failure of HTTP post
     */
    private void failureCallback() {
        if (o2mc != null) {
            o2mc.dispatchFailure();
        } else {
            Log.d(TAG, "O2mc variable is null.");
        }
    }
}
