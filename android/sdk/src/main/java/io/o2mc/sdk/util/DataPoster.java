package io.o2mc.sdk.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import io.o2mc.sdk.Datastreams.DatastreamsDispatcher;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class is used to send json payload to the backend server.
 */
public class DataPoster {
    private static DataPoster mInstance;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient client = new OkHttpClient().newBuilder().retryOnConnectionFailure(false).build();

    private static DatastreamsDispatcher datastreamsDispatcher;

    private DataPoster() {
    }

    public static DataPoster getInstance() {
        if (mInstance != null) {
            return mInstance;
        }

        synchronized (DataPoster.class) {
            if (mInstance == null) {
                mInstance = new DataPoster();
            }
        }

        return mInstance;
    }

    public static void setDatastreamsDispatcher(DatastreamsDispatcher datastreamsDispatcher) {
        DataPoster.datastreamsDispatcher = datastreamsDispatcher;
    }

    /**
     * Posts serialized JSON payload to a given end point.
     *
     * @param url  endpoint url.
     * @param json analytics payload.
     */
    public void post(String url, String json) {
        try {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    DataPoster.getInstance().failureCallback();

                    Log.w("DATA_POSTER", "Unable to post data.");
                    Log.w(DataPoster.class.getName(), e.getMessage());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (!response.isSuccessful()) return;

                    DataPoster.getInstance().successCallback();

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
            Log.e(DataPoster.class.getName(), e.getMessage());
        }
    }

    private void successCallback() {
        if (datastreamsDispatcher != null) {
            datastreamsDispatcher.callback();
        }
    }

    private void failureCallback() {
        if (datastreamsDispatcher != null) {
            datastreamsDispatcher.callbackFailure();
        }
    }
}
