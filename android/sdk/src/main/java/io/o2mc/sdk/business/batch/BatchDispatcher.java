package io.o2mc.sdk.business.batch;

import com.google.gson.Gson;
import io.o2mc.sdk.domain.Batch;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Dispatches events in JSON format.
 */
class BatchDispatcher {

  private static final String TAG = "BatchDispatcher";

  private Gson gson;

  private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private final OkHttpClient CLIENT =
      new OkHttpClient().newBuilder().retryOnConnectionFailure(false).build();

  private Callback callback;

  public BatchDispatcher(Callback callback) {
    this.callback = callback;
    gson = new Gson();
  }

  /**
   * Sends analytical events to backend specified by URL in JSON format.
   *
   * @param url url of backend to send events to
   * @param batch list of events with meta data
   */
  public void post(String url, Batch batch) {
    String json = batchToJson(batch);

    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder().url(url).post(body).build();

    CLIENT.newCall(request).enqueue(callback);
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
}
