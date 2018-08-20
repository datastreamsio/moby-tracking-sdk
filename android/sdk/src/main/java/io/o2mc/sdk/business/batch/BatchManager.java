package io.o2mc.sdk.business.batch;

import io.o2mc.sdk.Config;
import io.o2mc.sdk.TrackingManager;
import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.exceptions.O2MCEndpointException;
import io.o2mc.sdk.util.Util;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static io.o2mc.sdk.util.LogUtil.LogD;
import static io.o2mc.sdk.util.LogUtil.LogI;
import static io.o2mc.sdk.util.LogUtil.LogW;

/**
 * Manages everything that's related to batches by making use of a BatchBus and a BatchDispatcher.
 */
public class BatchManager extends TimerTask implements Callback {

  private static final String TAG = "BatchManager";

  private String endpoint;
  private boolean usingHttpsEndpoint;

  private int maxRetries; // max amount of times to retry sending batches
  private boolean isStopped;
  private boolean isFirstRun = true; // shows whether the manager has executed a dispatch yet

  private final Timer timer = new Timer(); // timer used for dispatching events
  private int dispatchInterval; // interval on which to send events

  private TrackingManager trackingManager;

  private BatchBus batchBus;

  private BatchDispatcher batchDispatcher;

  private String batchId;

  /**
   * @param trackingManager required for callbacks
   * @param endpoint URL to the backend
   * @param dispatchInterval dispatch interval in seconds
   * @param maxRetries number of times the manager will retry sending batches before giving up
   */
  public void init(TrackingManager trackingManager, String endpoint, int dispatchInterval,
      int maxRetries) {
    batchBus = new BatchBus();
    batchDispatcher = new BatchDispatcher(this);

    this.trackingManager = trackingManager;

    setEndpoint(endpoint);
    setDispatchInterval(dispatchInterval);
    setMaxRetries(maxRetries);
  }

  public void setIdentifier(String identifier) {
    this.batchId = identifier;
  }

  /**
   * Sets the max amount of retries for generating batches. Helps to reduce cpu usage / battery draining.
   *
   * @param maxRetries amount of times to retry before giving up
   */
  public void setMaxRetries(int maxRetries) {
    if (Util.isValidMaxRetries(maxRetries)) {
      this.maxRetries = maxRetries;
    } else {
      LogW(TAG,
          String.format(
              "setMaxRetries: Max retries amount '%s' is invalid. Setting to default '%s'",
              maxRetries, Config.DEFAULT_MAX_RETRIES));
      this.maxRetries = Config.DEFAULT_MAX_RETRIES;
    }
  }

  /**
   * Tells the EventManager on which intervals it should send the generated events.
   * Starts dispatching once interval is set.
   *
   * @param seconds time in seconds
   */
  private void setDispatchInterval(int seconds) {
    if (Util.isValidDispatchInterval(seconds)) {
      this.dispatchInterval = seconds;
      startDispatching(); // Interval is set, start dispatching now.
    } else {
      LogW(TAG, String.format(
          "setDispatchInterval: O2MC: Dispatch interval '%s' is invalid. Note that the value must be positive and is denoted in seconds.%nNot dispatching events. Setting to default '%s'",
          dispatchInterval, Config.DEFAULT_DISPATCH_INTERVAL));
      this.dispatchInterval = Config.DEFAULT_DISPATCH_INTERVAL;
    }
  }

  public String getEndpoint() {
    return endpoint;
  }

  /**
   * Checks whether or not the endpoint is a valid URL.
   *
   * @param endpoint URL in String format
   */
  public boolean setEndpoint(String endpoint) {
    if (Util.isValidEndpoint(endpoint)) {
      this.endpoint = endpoint;
      this.usingHttpsEndpoint = Util.isHttps(endpoint);
      return true;
    }

    trackingManager.notifyException(new O2MCEndpointException(
            String.format(
                "Endpoint is incorrect. Tracking events will fail to be dispatched. Please verify the correctness of '%s'.",
                endpoint)),
        true);  // fatal exception, the next dispatch wouldn't work even if we tried
    return false;
  }

  /**
   * Sets a timer for dispatching events to the backend.
   */
  private void startDispatching() {
    // Check if the device is allowed to dispatch events
    if (!Util.isAllowedToDispatchEvents(usingHttpsEndpoint)) {
      trackingManager.notifyException(new O2MCDispatchException(
              "Http traffic is not allowed on newer versions of the Android API. Please use HTTPS instead, or lower your min/target SDK version."),
          true); // fatal exception, the next dispatch wouldn't work even if we tried
      return;
    }

    final int SECOND = 1000;
    timer.schedule(this, dispatchInterval * SECOND, dispatchInterval * SECOND);
  }

  /**
   * Retrieves all events which are currently in the EventBus.
   *
   * @return a list of events -- possibly empty
   */
  private List<Event> getEvents() {
    return trackingManager.getEventsFromBus();
  }

  /**
   * Clears all events which are currently in the EventBus.
   */
  private void clearEvents() {
    trackingManager.clearEventsFromBus();
  }

  public void reset() {
    batchBus.clearBatches();
    batchBus.clearPending();
  }

  /**
   * Disallows generation and sending of batches.
   */
  public void stop() {
    reset();
    isStopped = true;
  }

  /**
   * Allows generating and sending of batches again.
   */
  public void resume() {
    isStopped = false;
  }

  /**
   * Callback from OkHttp, called after successfully sending an HTTP request
   *
   * @param call the HTTP call that was sent
   * @param response the HTTP response that was received
   */
  @Override public void onResponse(Call call, Response response) {
    if (response.isSuccessful()) {
      // Http response indicates success, inform user and SDK
      batchBus.onBatchSucceeded();
    } else {
      // Http response indicates failure, inform user and SDK
      dispatchFailed(new O2MCDispatchException(
          String.format("Backend HTTP response status code indicated failure. Status was '%s'",
              response.code())));
    }
  }

  /**
   * Callback from OkHttp, called upon failure while trying to send an HTTP request
   *
   * @param call the call that was tried to send
   * @param e the exception that occurred while trying to send
   */
  @Override public void onFailure(Call call, IOException e) {
    dispatchFailed(e);
  }

  /**
   * Called upon failure of HTTP post
   */
  private void dispatchFailed(Exception e) {
    batchBus.onBatchFailed();
    trackingManager.notifyException(new O2MCDispatchException(e),
        false); // non fatal, network may be down this time, but on next dispatch, the batch dispatch may work (again)
  }

  /**
   * Batch preparation and queuing logic.
   */
  @Override
  public void run() {
    if (isStopped) return;

    if (isFirstRun) {
      // Initialize batchGenerator meta data
      batchBus.setDeviceInformation(trackingManager.getDeviceInformation());
      isFirstRun = false;
    }

    // Don't try resending a batch if the max retries limit has exceeded
    if (batchBus.getRetries() > maxRetries) {
      LogW(TAG, "run: Max retries limit has been reached. Not trying to resend batch.");
      isStopped = true;
      return;
    }

    // Only generate a batch if we have events
    if (getEvents().size() > 0) {
      batchBus.add(batchBus.generateBatch(batchId, getEvents()));
      LogD(TAG,
          String.format("run: Newly generated batch contains '%s' events", getEvents().size()));
      clearEvents();
    }

    // If there's a batch pending, skip this run
    if (batchBus.awaitingCallback()) {
      LogD(TAG, "run: Still awaiting a callback from previous run. Stopping here.");
      return;
    }

    // There's no pending batch, set / generate one if possible
    if (batchBus.getPendingBatch() == null) {
      batchBus.setPendingBatch();
    }

    // If there is one now, send it
    if (batchBus.getPendingBatch() == null) {
      LogI(TAG, "run: There is no pending batch set. Not dispatching.");
      return;
    }

    // Dispatch the newly set batch
    LogI(TAG, String.format("run: Dispatching batch with '%s' events.",
        batchBus.getPendingBatch().getEvents().size()));
    batchBus.preDispatch();

    batchDispatcher.post(endpoint, batchBus.getPendingBatch());
  }
}
