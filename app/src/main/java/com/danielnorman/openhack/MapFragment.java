package com.danielnorman.openhack;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class MapFragment extends Fragment{
    //Creates objects
    MapView mapView;
    GoogleMap map;

    boolean addedMarkers = false;

    // Creates pointer to main activity
    MainActivity mMainActivity;

    public void setMainActivity(MainActivity mainActivity){
        this.mMainActivity = mainActivity;
        mMainActivity.mLocationHandler.getGeoPoint();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Gets the MapView from the XML layout and creates it
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // Initializes activity
        MapsInitializer.initialize(getActivity());

        // Processes different device states
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())){
            case ConnectionResult.SUCCESS:
                Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
                mapView = (MapView) v.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);

                // Gets to GoogleMap from the MapView and does initialization stuff
                if(mapView!=null){
                    map = mapView.getMap();
                    // UI settings
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    map.getUiSettings().setCompassEnabled(true);
                    map.setMyLocationEnabled(true);
                    // Finds the current location (testing: marker at current location)
                    ParseGeoPoint loc = mMainActivity.mLocationHandler.getGeoPoint();
                    //map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).);

                    // Zooms in on current location
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 18);
                    map.animateCamera(cameraUpdate);

                    //Add markers from ParseHandler
                    if (!addedMarkers) {
                        for (ParseObject post : mMainActivity.mParseHandler.getPostArrayList()) {
                            ParseGeoPoint geoPoint = post.getParseGeoPoint("locationGeoPoint");
                            map.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())));
                        }
                        addedMarkers = true;
                    }
                }
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), "Service Missing", Toast.LENGTH_LONG).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), "Update Required", Toast.LENGTH_LONG).show();
                break;
            default: Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }

        // Updates the map
        return v;
    }

    @Override
    public void onResume(){
        mapView.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void addMarkers() {
        if (map != null) {
            for (ParseObject post : mMainActivity.mParseHandler.getPostArrayList()) {
                ParseGeoPoint geoPoint = post.getParseGeoPoint("locationGeoPoint");
                map.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude())));
            }
            addedMarkers = true;
        }
    }
}
