package io.o2mc.sdk;

import android.app.Application;
import io.o2mc.sdk.business.DeviceManager;
import io.o2mc.sdk.business.batch.BatchManager;
import io.o2mc.sdk.business.event.EventManager;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.domain.Event;
import java.util.List;

/**
 * Acts as an intermediate between O2MC and our classes. This is useful to protect methods which
 * are not supposed to be used by the implementing App, but which are required to be public for
 * some function callbacks.
 * <p>
 * Protecting functions from use by an implementing App prevents accidental usage of methods and
 * therefore prevents incorrect usage of our SDK. Makes the SDK more easy to use and robust.
 */
public class TrackingManager {

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
   * Resets the current data recorded in the managers to zero. All events, batches, will be cleared.
   */
  public void reset() {
    this.eventManager.reset();
    this.batchManager.reset();
  }

  /**
   * Stops the EventManager and BatchManager from generating data and dispatching them.
   */
  public void stop() {
    eventManager.stop();
    batchManager.stop();
  }
}
