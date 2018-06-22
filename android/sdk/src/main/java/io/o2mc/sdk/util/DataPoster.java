package io.o2mc.sdk.util;

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

    private DataPoster() {}

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
     * @param url endpoint url.
     * @param json analytics payload.
     * @throws IOException
     */
    public void post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                DataPoster.getInstance().failureCallback();

                Log.w("DATA_POSTER", "Unable to post data.");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) return;

                DataPoster.getInstance().successCallback();
                Log.e("POSTED", "payload: " + response.body().string());
            }
        });
    }

    public void successCallback() {
        if (datastreamsDispatcher != null) {
            datastreamsDispatcher.callback();
        }
    }

    public void failureCallback() {
        if (datastreamsDispatcher != null) {
            datastreamsDispatcher.callbackFailure();
        }
    }
}
