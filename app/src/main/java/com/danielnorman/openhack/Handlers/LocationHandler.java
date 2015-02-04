package com.danielnorman.openhack.Handlers;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseGeoPoint;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

/**
 * Created by Daniel on 1/30/15.
 */
public class LocationHandler implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {
    private Context mContext;
    private Location mLocation;
    private ParseGeoPoint mGeoPoint;

    public GoogleApiClient mGoogleApiClient;
    public LocationRequest mLocationRequest;


    public LocationHandler(Context mContext) {
        this.mContext = mContext;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000 * 60 * 2);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        buildGoogleApiClient();
    }


    public ParseGeoPoint getGeoPoint() {
        return mGeoPoint;
    }

    public void setGeoPoint(Location location) {
        mLocation = location;
        mGeoPoint = new ParseGeoPoint(mLocation.getLatitude(), mLocation.getLongitude());
        System.out.println("Location set: " + mGeoPoint);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        setGeoPoint(location);
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
