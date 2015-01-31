package com.danielnorman.openhack;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * Created by Daniel on 1/30/15.
 */
public class LocationHandler {
    private Context mContext;
    private LocationManager mLocationManager;
    private Location mLocation;


    public LocationHandler(Context mContext) {
        this.mContext = mContext;
        this.mLocationManager = (LocationManager) this.mContext.getSystemService(mContext.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = mLocationManager.getBestProvider(criteria, true);
        this.mLocation = mLocationManager.getLastKnownLocation(provider);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListenerGps);
    }

    public Location getLocation()
    {
        return mLocation;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) mLocation = location;
            System.out.println("Print location");
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

}
