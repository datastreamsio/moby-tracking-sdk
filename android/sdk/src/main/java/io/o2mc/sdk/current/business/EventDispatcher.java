package io.o2mc.sdk.current.business;

import java.util.List;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

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
     * Sends analytical events to specified backend in JSON format.
     *
     * @param url    url of backend to send events to
     * @param events list of Event objects
     */
    public void post(String url, List<Event> events) {
        try {
            String json = eventsToJson(events);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(body).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    EventDispatcher.getInstance().failureCallback();

                    Log.w("DATA_POSTER", "Unable to post data.");
                    Log.w(TAG, e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (!response.isSuccessful()) return;

                    EventDispatcher.getInstance().successCallback();

                    if (response.body() == null) {
                        Log.e("POSTED", "payload: <EMPTY RESPONSE BODY>");
                    } else {
                        //noinspection ConstantConditions
                        Log.d("POSTED", "payload: " + response.body().toString());
                    }
                }
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            this.failureCallback();
            Log.e(TAG, e.getMessage());
        }
    }

    private String eventsToJson(List<Event> events) {
        // todo; obviously, this method has to be implemented. leaving like this for dev purposes
        StringBuilder result = new StringBuilder("nothing");
        for (Event e : events) {
            String json = gson.toJson(e);
            Log.d(TAG, String.format("Turning event %s into JSON String", e.toString()));
            Log.d(TAG, String.format("Json string: < %s >", json));
            result.append(json);
        }
        return result.toString();
    }

    public static void setO2mc(O2mc o2mc) {
        EventDispatcher.o2mc = o2mc;
        Log.d(TAG, "Set o2mc field.");
    }

    private void successCallback() {
        if (o2mc != null) {
            o2mc.dispatchSuccess();
        } else {
            Log.d(TAG, "O2mc variable is null.");
        }
    }

    private void failureCallback() {
        if (o2mc != null) {
            o2mc.dispatchFailure();
        } else {
            Log.d(TAG, "O2mc variable is null.");
        }
    }
}
