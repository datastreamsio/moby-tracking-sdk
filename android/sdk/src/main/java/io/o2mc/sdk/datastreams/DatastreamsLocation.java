package io.o2mc.sdk.datastreams;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Location listener class. it updates location coordinates the rootobject
 */
public class DatastreamsLocation implements LocationListener {

    private Datastream datastream;

    public DatastreamsLocation(Datastream ds) {
        datastream = ds;
    }

    @Override
    public void onLocationChanged(Location location) {
        datastream.setLatitude(location.getLatitude());
        datastream.setLongitude(location.getLongitude());
        Log.d("LatLong", datastream.getLatitude() + "," + datastream.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}