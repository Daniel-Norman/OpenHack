package com.danielnorman.openhack;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.danielnorman.openhack.Handlers.PostContainer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.HashMap;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    //Creates objects
    MapView mapView;
    GoogleMap map;
    HashMap<Marker, PostContainer> mPostMarkerMap = new HashMap<>();

    // Creates pointer to main activity
    MainActivity mMainActivity;

    public void setMainActivity(MainActivity mainActivity){
        this.mMainActivity = mainActivity;
        mMainActivity.mLocationHandler.getGeoPoint();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Gets the MapView from the XML layout and creates it
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        // Initializes activity
        MapsInitializer.initialize(getActivity());

        // Processes different device states
        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())){
            case ConnectionResult.SUCCESS:
                //Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
                mapView = (MapView) v.findViewById(R.id.map);
                mapView.onCreate(savedInstanceState);

                // Gets to GoogleMap from the MapView and does initialization stuff
                if(mapView!=null){
                    map = mapView.getMap();
                    // UI settings
                    map.getUiSettings().setMyLocationButtonEnabled(true);
                    map.getUiSettings().setCompassEnabled(true);
                    map.getUiSettings().setMapToolbarEnabled(false);
                    map.setMyLocationEnabled(true);
                    map.setOnMarkerClickListener(this);
                    // Finds the current location (testing: marker at current location)
                    ParseGeoPoint loc = mMainActivity.mLocationHandler.getGeoPoint();

                    // Zooms in on current location
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 18);
                    map.animateCamera(cameraUpdate);

                    //Add markers from ParseHandler
                    addMarkers();

                    // Setting a custom info window adapter for the google map
                    map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        // Use default InfoWindow frame
                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        // Defines the contents of the InfoWindow
                        @Override
                        public View getInfoContents(Marker marker) {

                            // Getting view from the layout file info_window_layout
                            View v = inflater.inflate(R.layout.map_window_layout, null);

                            // Getting reference to the TextView to set latitude
                            TextView captionTextView = (TextView) v.findViewById(R.id.map_window_textview);

                            // Getting reference to the TextView to set longitude
                            ImageView imageView = (ImageView) v.findViewById(R.id.map_window_imageview);

                            captionTextView.setText(mPostMarkerMap.get(marker).getParseObject().getString("caption"));

                            Bitmap bitmap = mPostMarkerMap.get(marker).getBitmap();

                            imageView.setImageBitmap(bitmap);

                            // Returning the view containing InfoWindow contents
                            return v;

                        }
                    });
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
        super.onResume();
        mapView.onResume();
        addMarkers();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
        mPostMarkerMap.clear();
    }
    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println(mPostMarkerMap.get(marker).getParseObject().getString("caption"));
        marker.showInfoWindow();
        return true;
    }

    public void addMarkers() {
        if (map != null) {
            for (Marker marker : mPostMarkerMap.keySet()) {
                marker.remove();
            }
            for (PostContainer post : mMainActivity.mParseHandler.getPostMap().values()) {
                ParseGeoPoint geoPoint = post.getParseObject().getParseGeoPoint("locationGeoPoint");
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))
                        .title(post.getParseObject().getString("caption")));
                mPostMarkerMap.put(marker, post);
            }
        }
    }
}
