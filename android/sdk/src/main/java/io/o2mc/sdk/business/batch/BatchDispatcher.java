package io.o2mc.sdk.business.batch;

import com.google.gson.Gson;
import io.o2mc.sdk.O2MCCallback;
import io.o2mc.sdk.domain.Batch;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static io.o2mc.sdk.util.LogUtil.LogD;

/**
 * Dispatches events in JSON format.
 * Singleton class, guaranteed to have only one instance in the apps lifecycle.
 * Sends events in a thread-safe manner.
 */
class BatchDispatcher {

  private static final String TAG = "BatchDispatcher";

  private static Gson gson;
  private static BatchManager batchManager;

  private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  private static final OkHttpClient client =
      new OkHttpClient().newBuilder().retryOnConnectionFailure(false).build();

  // ==========================================
  // region Start singleton technicalities
  // ==========================================
  private static BatchDispatcher instance;

  public static synchronized BatchDispatcher getInstance() {
    if (instance == null) {
      instance = new BatchDispatcher();
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
   * @param url url of backend to send events to
   * @param batch list of events with meta data
   */
  public void post(String url, Batch batch, O2MCCallback callback) {
    String json = batchToJson(batch);

    RequestBody body = RequestBody.create(JSON, json);
    Request request = new Request.Builder().url(url).post(body).build();

    client.newCall(request).enqueue(new BatchCallback(callback));
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

  public void setBatchManager(BatchManager batchManager) {
    BatchDispatcher.batchManager = batchManager;
    LogD(TAG, "Set o2mc field.");
  }
}
