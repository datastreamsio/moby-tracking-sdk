package io.o2mc.sdk;

import android.app.Application;
import io.o2mc.sdk.business.DeviceManager;
import io.o2mc.sdk.business.batch.BatchManager;
import io.o2mc.sdk.business.event.EventManager;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.domain.Event;
import io.o2mc.sdk.interfaces.O2MCExceptionListener;
import io.o2mc.sdk.util.Util;
import java.util.List;

import static io.o2mc.sdk.util.LogUtil.LogD;

/**
 * Acts as an intermediate between O2MC and our classes. This is useful to protect methods which
 * are not supposed to be used by the implementing App, but which are required to be public for
 * some function callbacks.
 * <p>
 * Protecting functions from use by an implementing App prevents accidental usage of methods and
 * therefore prevents incorrect usage of our SDK. Makes the SDK more easy to use and robust.
 */
public class TrackingManager {

  private static final String TAG = "TrackingManager";

  private Application application;

  private DeviceManager deviceManager;
  private EventManager eventManager;
  private BatchManager batchManager;

  private DeviceInformation deviceInformation;

  public TrackingManager(Application application, String endpoint, int dispatchInterval,
      int maxRetries) {
    this.application = application;

    this.deviceManager = new DeviceManager(application);
    this.eventManager = new EventManager();
    this.batchManager = new BatchManager(this, endpoint, dispatchInterval, maxRetries);
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
    deviceManager.setO2MCExceptionListener(o2MCExceptionListener);
    batchManager.setO2MCExceptionListener(o2MCExceptionListener);
    eventManager.setO2MCExceptionListener(o2MCExceptionListener);
  }

  public void setIdentifier(String identifier) {
    batchManager.setIdentifier(identifier);
  }

  public void setSessionIdentifier() {
    batchManager.setIdentifier(Util.generateUUID());
  }
}
