package com.danielnorman.openhack.Handlers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.parse.ParseGeoPoint;

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
    private ParseGeoPoint mGeoPoint;


    public LocationHandler(Context mContext) {
        this.mContext = mContext;
        this.mLocationManager = (LocationManager) this.mContext.getSystemService(mContext.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String provider = mLocationManager.getBestProvider(criteria, true);
        this.mLocation = mLocationManager.getLastKnownLocation(provider);
        if (this.mLocation != null) {
            this.mGeoPoint = new ParseGeoPoint(this.mLocation.getLatitude(), this.mLocation.getLongitude());
        } else {
            this.mGeoPoint = null;
        }

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListenerGps);
    }

    public ParseGeoPoint getGeoPoint() {
        return mGeoPoint;
    }
    public Location getLocation() {
        return mLocation;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (location != null) {
                mLocation = location;
                if (mLocation != null) {
                    mGeoPoint = new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
                }            }
            System.out.println("Print location");
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

}
