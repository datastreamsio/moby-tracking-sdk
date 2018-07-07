package io.o2mc.sdk;

import android.app.Application;

import java.util.List;

import io.o2mc.sdk.business.DeviceManager;
import io.o2mc.sdk.business.batch.BatchDispatcher;
import io.o2mc.sdk.business.batch.BatchManager;
import io.o2mc.sdk.business.event.EventManager;
import io.o2mc.sdk.domain.DeviceInformation;
import io.o2mc.sdk.domain.Event;

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

    public TrackingManager(Application application, String endpoint, int dispatchInterval, int maxRetries) {
        this.application = application;

        this.deviceManager = new DeviceManager(application);
        this.eventManager = new EventManager();
        this.batchManager = new BatchManager(this, endpoint, dispatchInterval, maxRetries);
    }

    public void track(String eventName) {
        eventManager.newEvent(eventName);
    }

    public void trackWithProperties(String eventName, String value) {
        eventManager.newEventWithProperties(eventName, value);
    }

    public List<Event> getEventsFromBus() {
        return eventManager.getEventsFromBus();
    }

    public void clearEventsFromBus() {
        eventManager.clearEventsFromBus();
    }

    /**
     * Return device information. Generate it it it hasn't been generated before.
     *
     * @return information about device
     */
    public DeviceInformation getDeviceInformation() {
        if (deviceInformation == null) {
            this.deviceInformation = deviceManager.generateDeviceInformation();
        }
        return deviceInformation;
    }

    public void reset() {
        this.eventManager.reset();
        this.batchManager.reset();
    }
}
