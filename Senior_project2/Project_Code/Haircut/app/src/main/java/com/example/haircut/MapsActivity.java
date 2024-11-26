package com.example.haircut;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    // Define the bounds for Jeddah
    private static final LatLngBounds JEDDAH_BOUNDS = new LatLngBounds(
            new LatLng(21.1246, 39.0975), // Southwest corner (extended)
            new LatLng(21.8244, 39.5731)  // Northeast corner (extended)
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDkjclvFuCZBIxcEarf0WNtF3Veesh2wHs");
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the search bar and set up autocomplete support
        setupAutoComplete();

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setup the FloatingActionButton to center the map
        findViewById(R.id.btnCenterMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerMapToCurrentLocation();
            }
        });
    }

    private void setupAutoComplete() {
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            // Restrict the autocomplete results to Jeddah bounds
            autocompleteFragment.setLocationBias(com.google.android.libraries.places.api.model.RectangularBounds.newInstance(
                    JEDDAH_BOUNDS.southwest, JEDDAH_BOUNDS.northeast
            ));

            // Set place fields to retrieve only necessary data
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.TYPES));

            // Restrict to Saudi Arabia
            autocompleteFragment.setCountries("SA");

            // Set up place selection listener
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    if (place.getLatLng() != null && place.getName() != null) {
                        LatLng selectedLatLng = place.getLatLng();

                        // Keywords to identify barbershops
                        String placeName = place.getName().toLowerCase();
                        boolean hasRelevantKeyword = placeName.contains("barber") || placeName.contains("salon") || placeName.contains("saloon") ||
                        placeName.contains("حلاق") || placeName.contains("صالون");

                        // Place types to identify barbershops
                        boolean hasRelevantType = place.getTypes() != null &&
                                (place.getTypes().contains(Place.Type.BEAUTY_SALON) ||
                                        place.getTypes().contains(Place.Type.HAIR_CARE));

                        if (JEDDAH_BOUNDS.contains(selectedLatLng) && (hasRelevantKeyword || hasRelevantType)) {
                            // Move camera and add marker
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                            mMap.addMarker(new MarkerOptions().position(selectedLatLng).title(place.getName()));
                        } else {
                            Toast.makeText(MapsActivity.this, "Selected place is not a barbershop!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onError(@NonNull Status status) {
                    Toast.makeText(MapsActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void centerMapToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        if (JEDDAH_BOUNDS.contains(currentLocation)) {
                            // Move camera to current location
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                        } else {
                            Toast.makeText(MapsActivity.this, "You are outside Jeddah!", Toast.LENGTH_SHORT).show();
                            // Move camera to Jeddah's center
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(JEDDAH_BOUNDS.getCenter(), 12));
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Enable zoom controls
            mMap.getUiSettings().setZoomControlsEnabled(true);

            // Restrict the camera to Jeddah
            mMap.setLatLngBoundsForCameraTarget(JEDDAH_BOUNDS);
            mMap.setMinZoomPreference(10.0f);
            mMap.setMaxZoomPreference(20.0f);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            mMap.setMyLocationEnabled(true);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing map: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
