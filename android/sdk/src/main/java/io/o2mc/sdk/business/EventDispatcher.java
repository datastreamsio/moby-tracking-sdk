package io.o2mc.sdk.business;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import io.o2mc.sdk.O2MC;
import io.o2mc.sdk.domain.Batch;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Dispatches events in JSON format.
 * Singleton class, guaranteed to have only one instance in the apps lifecycle.
 * Sends events in a thread-safe manner.
 */
public class EventDispatcher {

    private static final String TAG = "EventDispatcher";

    private static Gson gson;
    private static O2MC o2mc;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(false).build();

    // ==========================================
    // region Start singleton technicalities
    // ==========================================
    private static EventDispatcher instance;

    public static synchronized EventDispatcher getInstance() {
        if (instance == null) {
            instance = new EventDispatcher();
            gson = new Gson();
        }
        return instance;
    }
    // ==========================================
    // endregion Start singleton technicalities
    // ==========================================

    /**
     * Sends analytical events to backend specified by URL in JSON format.
     *
     * @param url   url of backend to send events to
     * @param batch list of events with meta data
     */
    public void post(String url, Batch batch) {
        try {
            String json = batchToJson(batch);

            Log.d(TAG, String.format("Posting batch containing a total of '%s' characters", json.length()));
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
                            try {
                                Log.i(TAG, String.format("onResponse: http response contained '%s' characters", response.body().string().length()));
                            } catch (NullPointerException | IOException ex) {
                                Log.e(TAG, "onResponse: Response string is null", ex);
                            }
                        }
                    } else {
                        try {
                            // Http response indicates failure, inform user and SDK
                            EventDispatcher.getInstance().failureCallback();
                            Log.w(TAG, String.format("onResponse: Http response indicates failure: '%s'", response.body().string()));
                        } catch (NullPointerException | IOException ex) {
                            Log.e(TAG, "onResponse: Response string is null", ex);
                        }
                    }
                }
            });
        } catch (IllegalArgumentException | NullPointerException e) {
            EventDispatcher.getInstance().failureCallback();
            Log.e(TAG, "post: Failed to dispatch events", e);
        }
    }

    /**
     * Transforms a list of events to an array of JsonObjects in Json format
     *
     * @param batch list of events with meta data
     * @return list of JsonObjects in JSON format, as String
     */
    private String batchToJson(Batch batch) {
        return gson.toJson(batch);
    }

    public void setO2mc(O2MC o2mc) {
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
