package io.o2mc.sdk;

import android.app.Application;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;
import io.o2mc.sdk.util.LogUtil;

import static io.o2mc.sdk.util.LogUtil.LogD;

/**
 * This is central point of communication between the SDK and the app implementing it.
 * The implementing app should never have anything to deal with any other class than this one.
 */
// Suppress unused warnings, because the methods in this class are supposed to be used by any
// app implementing this SDK. They may or may not be used, but that's up to the developer.
@SuppressWarnings({ "unused", "WeakerAccess" }) public class O2MC {

  private static final String TAG = "O2MC";

  @SuppressWarnings("FieldCanBeLocal")
  // keeping the variable here may prevent GC from removing the reference to the App
  // required for activity callbacks & context (may need it for more callbacks in the future)
  private Application app;

  private TrackingManager trackingManager;

  /**
   * This is central point of communication between the SDK and the app implementing it.
   * The implementing app should never have anything to deal with any other class than this one.
   *
   * @param app Top-level application class, as defined in the app manifest. Used to automatically detect meta-events like ActivityStarted and ActivityDestroyed.
   * @param endpoint URL to the back-end, defines where to dispatch tracking events to.
   */
  public O2MC(Application app, String endpoint) {
    this(app, endpoint, Config.DEFAULT_DISPATCH_INTERVAL, Config.DEFAULT_MAX_RETRIES);
  }

  /**
   * This is central point of communication between the SDK and the app implementing it.
   * The implementing app should never have anything to deal with any other class than this one.
   *
   * @param app Top-level application class, as defined in the app manifest
   * @param endpoint URL to the back-end, defines where to dispatch tracking events to
   * @param dispatchInterval Tells the EventManager on which intervals it should send the generated events. Denoted in seconds.
   */
  public O2MC(Application app, String endpoint, int dispatchInterval) {
    this(app, endpoint, dispatchInterval, Config.DEFAULT_MAX_RETRIES);
  }

  /**
   * This is central point of communication between the SDK and the app implementing it.
   * The implementing app should never have anything to deal with any other class than this one.
   *
   * @param app Top-level application class, as defined in the app manifest
   * @param endpoint URL to the back-end, defines where to dispatch tracking events to
   * @param dispatchInterval Tells the EventManager on which intervals it should send the generated events. Denoted in seconds.
   * @param maxRetries Sets the max amount of retries for generating batches. Helps to reduce cpu usage / battery draining.
   */
  public O2MC(Application app, String endpoint, int dispatchInterval, int maxRetries) {
    trackingManager = new TrackingManager();
    trackingManager.init(app, endpoint, dispatchInterval, maxRetries);
  }

  /**
   * Retrieves the URL of the backend to send batches to.
   *
   * @param endpoint String value of the URL to the backend
   */
  public String getEndpoint(String endpoint) {
    return trackingManager.getEndpoint();
  }

  /**
   * Changes the URL of the backend to send batches to.
   *
   * @param endpoint String value of the URL to the backend
   */
  public void setEndpoint(String endpoint) {
    boolean validEndpoint = trackingManager.setEndpoint(endpoint);
    if (validEndpoint) {
      // Manager may be stopped earlier because the endpoint was incorrect
      // Since the endpoint is correct now, resume tracking
      trackingManager.resume();
    } else {
      // Stop tracking if the endpoint is invalid. Events won't be sent to the
      // backend anyway, it would only waste the user's data bundle by trying.
      trackingManager.stop();
    }
  }

  /**
   * Enables or disables automatic activity lifecycle event tracking.
   * If set to true, activity life cycle events like 'onCreate(...)' are automatically
   * tracked and sent to the backend.
   *
   * @param shouldTrackContext true if you wish to track context
   */
  public void setContextTracking(boolean shouldTrackContext) {
    if (shouldTrackContext) {
      trackingManager.startLifecycleTracking();
    } else {
      trackingManager.stopLifecycleTracking();
    }
  }

  /**
   * Changes the number of times the SDK should try to resend data to the backend before giving up.
   *
   * @param maxRetries number of times to retry
   */
  public void setMaxRetries(int maxRetries) {
    trackingManager.setMaxRetries(maxRetries);
  }

  /**
   * Sets an exception listener which receives any exceptions thrown by the O2MC module.
   *
   * @param o2MCExceptionListener any class implementing the `O2MCExceptionListener` interface
   */
  public void setO2MCExceptionListener(O2MCExceptionListener o2MCExceptionListener) {
    trackingManager.setO2MCExceptionListener(o2MCExceptionListener);
  }

  /**
   * Sets an identifier for a user. This identifier will be sent along with tracked events.
   * This could be useful to correlate various batches with each other.
   *
   * @param identifier any identifier you'd like to use to identify a user with (in String format)
   */
  public void setIdentifier(String identifier) {
    trackingManager.setIdentifier(identifier);
  }

  /**
   * Sets a randomly generated UUID for a user. This identifier will be sent along with tracked events.
   * This could be useful to correlate various batches with each other.
   */
  public void setSessionIdentifier() {
    trackingManager.setSessionIdentifier();
  }

  /**
   * Enable or disable logging.
   * Logging in release builds is disabled. This behavior is immutable.
   * Logging in all other builds is configurable. The default is set to 'true', logging is enabled.
   *
   * @param shouldLog true if logging should be enabled, false if logging should be disabled
   */
  public void setLogging(boolean shouldLog) {
    LogUtil.setShouldLog(shouldLog);
  }

  /**
   * Tracks an event.
   * Essentially adds a new event with the String parameter as name to be dispatched on the next dispatch interval.
   *
   * @param eventName name of tracked event
   */
  public void track(String eventName) {
    LogD(TAG, String.format("Tracked '%s'", eventName));
    trackingManager.track(eventName);
  }

  /**
   * Tracks an event with additional data.
   * Essentially adds a new event with the String parameter as name and any properties.
   * Will be dispatched to backend on next dispatch interval.
   *
   * @param eventName name of tracked event
   * @param value anything you'd like to keep track of in String format
   */
  public void trackWithProperties(String eventName, Object value) {
    trackingManager.trackWithProperties(eventName, value);
  }

  /**
   * Removes events that were cached and would have been sent on the next interval otherwise.
   * Additionally disallows generating new events created by the 'track' methods.
   * Stops sending events to the backend altogether.
   */
  public void stop() {
    trackingManager.stop();
  }

  /**
   * Allows generating new events created by the 'track' methods.
   * Starts sending events to the backend again.
   */
  public void resume() {
    trackingManager.resume();
  }
}
