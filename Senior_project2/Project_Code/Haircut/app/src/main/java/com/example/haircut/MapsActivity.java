package com.example.haircut;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize the SupportMapFragment and get the map asynchronously
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Coordinates for Jeddah, Saudi Arabia
        LatLng jeddah = new LatLng(21.2854, 39.2376);

        // Add a marker for Jeddah and move the camera to it
        mMap.addMarker(new MarkerOptions().position(jeddah).title("Marker in Jeddah"));
        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Set the zoom level to zoom out
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jeddah, 8));  // Lower zoom level (zoom out)
    }
}
