package io.o2mc.sdk;

import android.app.Application;
import io.o2mc.sdk.business.DeviceManager;
import io.o2mc.sdk.business.batch.BatchManager;
import io.o2mc.sdk.business.event.EventManager;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.exceptions.O2MCDeviceException;
import io.o2mc.sdk.exceptions.O2MCDispatchException;
import io.o2mc.sdk.exceptions.O2MCEndpointException;
import io.o2mc.sdk.exceptions.O2MCException;
import io.o2mc.sdk.exceptions.O2MCTrackException;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;
import io.o2mc.sdk.interfaces.O2MCExceptionNotifier;
import io.o2mc.sdk.util.Util;
import java.util.List;

import static io.o2mc.sdk.util.LogUtil.LogD;
import static io.o2mc.sdk.util.LogUtil.LogE;
import static io.o2mc.sdk.util.LogUtil.LogW;

/**
 * Acts as an intermediate between O2MC and our classes. This is useful to protect methods which
 * are not supposed to be used by the implementing App, but which are required to be public for
 * some function callbacks.
 * <p>
 * Protecting functions from use by an implementing App prevents accidental usage of methods and
 * therefore prevents incorrect usage of our SDK. Makes the SDK more easy to use and robust.
 */
public class TrackingManager implements O2MCExceptionNotifier {

  private static final String TAG = "TrackingManager";

  private Application application;

  private DeviceManager deviceManager;
  private EventManager eventManager;
  private BatchManager batchManager;

  private DeviceInformation deviceInformation;

  private O2MCExceptionListener o2MCExceptionListener;

  public void init(Application application, String endpoint, int dispatchInterval,
      int maxRetries) {
    this.application = application;

    this.deviceManager = new DeviceManager();
    this.eventManager = new EventManager();
    this.batchManager = new BatchManager();

    this.deviceManager.init(this, application);
    this.eventManager.init(this);
    this.batchManager.init(this, endpoint, dispatchInterval, maxRetries);
  }

  public void setMaxRetries(int maxRetries) {
    this.batchManager.setMaxRetries(maxRetries);
  }

  public void track(String eventName) {
    eventManager.newEvent(eventName);
  }

  public void trackWithProperties(String eventName, String value) {
    eventManager.newEventWithProperties(eventName, value);
  }

  public List<Event> getEventsFromBus() {
    return eventManager.getEvents();
  }

  public void clearEventsFromBus() {
    eventManager.reset();
  }

  /**
   * Return device information. Generate if it hasn't been generated before.
   *
   * @return information about device
   */
  public DeviceInformation getDeviceInformation() {
    if (deviceInformation == null) {
      deviceInformation = deviceManager.generateDeviceInformation();
    }
    return deviceInformation;
  }

  /**
   * Stops the EventManager and BatchManager from generating data and dispatching them.
   */
  public void stop() {
    eventManager.stop();
    batchManager.stop();
    LogD(TAG, "Stop generating and dispatching events / batches.");
  }

  /**
   * Allows the EventManager and BatchManager to execute their tasks (again).
   */
  public void resume() {
    eventManager.resume();
    batchManager.resume();
    LogD(TAG, "Resumed generating and dispatching events / batches.");
  }

  public void setO2MCExceptionListener(O2MCExceptionListener o2MCExceptionListener) {
    this.o2MCExceptionListener = o2MCExceptionListener;
  }

  public void setIdentifier(String identifier) {
    batchManager.setIdentifier(identifier);
  }

  public void setSessionIdentifier() {
    batchManager.setIdentifier(Util.generateUUID());
  }

  @Override public void notifyException(O2MCException e, boolean isFatal) {
    if (o2MCExceptionListener != null) { // if listener is set, inform using an exception

      // Important: Check every class
      if (e instanceof O2MCEndpointException) {
        o2MCExceptionListener.onO2MCEndpointException((O2MCEndpointException) e);
      } else if (e instanceof O2MCDeviceException) {
        o2MCExceptionListener.onO2MCDeviceException((O2MCDeviceException) e);
      } else if (e instanceof O2MCDispatchException) {
        o2MCExceptionListener.onO2MCDispatchException((O2MCDispatchException) e);
      } else if (e instanceof O2MCTrackException) {
        o2MCExceptionListener.onO2MCTrackException((O2MCTrackException) e);
      } else {
        LogE(TAG, String.format(
            "Exception \"%s\" (\"%s\") is not explicitly handled by the TrackingManager. Please notify the maintainer of the SDK to update the exception notifier.",
            e.getClass().getSimpleName(), e.getMessage()));
      }
    } else { // no listener set, just log
      LogE(TAG, e.getMessage());
    }

    // If exception was fatal, stop the SDK
    if (isFatal) {
      stop();
      LogW(TAG, String.format("Exception \"%s\" was fatal. SDK was stopped.", e.getClass().getSimpleName()));
    }
  }
}
