package io.o2mc.sdk.business.batch;

import io.o2mc.sdk.Config;
import io.o2mc.sdk.TrackingManager;
import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.util.Util;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static io.o2mc.sdk.util.LogUtil.LogD;
import static io.o2mc.sdk.util.LogUtil.LogE;
import static io.o2mc.sdk.util.LogUtil.LogI;
import static io.o2mc.sdk.util.LogUtil.LogW;

/**
 * Manages everything that's related to batches by making use of a BatchBus and a BatchDispatcher.
 */
public class BatchManager {

  private static final String TAG = "BatchManager";

  private String endpoint;
  private boolean usingHttpsEndpoint;

  private int maxRetries; // max amount of times to retry sending batches

  private final Timer timer = new Timer(); // timer used for dispatching events
  private int dispatchInterval; // interval on which to send events

  private TrackingManager trackingManager;

  private BatchBus batchBus;

  /**
   * @param trackingManager required for callbacks
   * @param endpoint URL to the backend
   * @param dispatchInterval dispatch interval in seconds
   * @param maxRetries number of times the manager will retry sending batches before giving up
   */
  public BatchManager(TrackingManager trackingManager, String endpoint, int dispatchInterval,
      int maxRetries) {
    this.batchBus = new BatchBus();

    setEndpoint(endpoint);
    setDispatchInterval(dispatchInterval);
    setMaxRetries(maxRetries);

    this.trackingManager = trackingManager;

    BatchDispatcher.getInstance().setBatchManager(this);
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
      LogE(TAG, String.format("O2MC: Max retries amount '%s' is invalid.", maxRetries));
      LogW(TAG,
          String.format("setMaxRetries: Setting to default '%s'", Config.DEFAULT_MAX_RETRIES));
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
      LogE(TAG, String.format(
          "O2MC: Dispatch interval '%s' is invalid. Note that the value must be positive and is denoted in seconds.%nNot dispatching events.",
          dispatchInterval));
      LogW(TAG, String.format("setDispatchInterval: Setting to default '%s'",
          Config.DEFAULT_DISPATCH_INTERVAL));
      this.dispatchInterval = Config.DEFAULT_DISPATCH_INTERVAL;
    }
  }

  /**
   * Checks whether or not the endpoint is a valid URL.
   *
   * @param endpoint URL in String format
   */
  private void setEndpoint(String endpoint) {
    if (endpoint == null || endpoint.isEmpty()) {
      LogE(TAG, "O2MC: Please provide a non-empty endpoint.");
    } else if (Util.isValidEndpoint(endpoint)) {
      this.endpoint = endpoint;
      this.usingHttpsEndpoint = Util.isHttps(endpoint);
    } else {
      LogE(TAG, String.format(
          "O2MC: Endpoint is incorrect. Tracking events will fail to be dispatched. Please verify the correctness of '%s'.",
          endpoint));
    }
  }

  /**
   * Called upon successful HTTP post
   */
  public void dispatchSuccess() {
    batchBus.lastBatchSucceeded();
  }

  /**
   * Called upon failure of HTTP post
   */
  public void dispatchFailure() {
    batchBus.lastBatchFailed();
  }

  /**
   * Sets a timer for dispatching events to the backend.
   */
  private void startDispatching() {
    // Check if the device is allowed to dispatch events
    if (!Util.isAllowedToDispatchEvents(usingHttpsEndpoint)) {
      LogE(TAG, "run: Not allowed to dispatch events. See previous message(s) for more info.");
      return;
    }

    final int SECOND = 1000;
    timer.schedule(new DispatcherTask(), dispatchInterval * SECOND, dispatchInterval * SECOND);
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

  public void stop() {
    stopTimer();
    reset();
  }

  private void stopTimer() {
    timer.cancel();
    timer.purge();
  }

  /**
   * Sends all events from the EventBus to the backend, if there are any events.
   */
  class DispatcherTask extends TimerTask {
    private static final String TAG = "Dispatcher";

    @Override
    public void run() {
      // Initialize batchGenerator meta data
      batchBus.setDeviceInformation(trackingManager.getDeviceInformation());

      // Don't try resending a batch if the max retries limit has exceeded
      if (batchBus.getRetries() > maxRetries) {
        LogW(TAG, "run: Max retries limit has been reached. Not trying to resend batch.");
        stopTimer();
        return;
      }

      // Only generate a batch if we have events
      if (getEvents().size() > 0) {
        batchBus.add(batchBus.generateBatch(getEvents()));
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
      batchBus.onDispatch();
      BatchDispatcher.getInstance().post(endpoint, batchBus.getPendingBatch());
    }
  }
}
