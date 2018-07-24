package io.o2mc.sdk.business.batch;

import com.google.gson.Gson;
import io.o2mc.sdk.domain.Batch;
import io.o2mc.sdk.interfaces.O2MCCallback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Dispatches events in JSON format.
 * Singleton class, guaranteed to have only one instance in the apps lifecycle.
 */
class BatchDispatcher {

  private Gson gson;

  private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private final OkHttpClient CLIENT =
      new OkHttpClient().newBuilder().retryOnConnectionFailure(false).build();

  public BatchDispatcher() {
    gson = new Gson();
  }

  /**
   * Sends analytical events to backend specified by URL in JSON format.
   *
   * @param url url of backend to send events to
   * @param batch list of events with meta data
   */
  public void post(String url, Batch batch, O2MCCallback callback) {
    String json = batchToJson(batch);

    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder().url(url).post(body).build();

    CLIENT.newCall(request).enqueue(new BatchCallback(callback));
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
