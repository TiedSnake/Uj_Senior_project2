package com.example.haircut;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.haircut.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SearchView searchView;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map2);

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDkjclvFuCZBIxcEarf0WNtF3Veesh2wHs"); // Replace with your API key
        }

        // Initialize PlacesClient
        placesClient = Places.createClient(this);

        // Initialize the SearchView
        searchView = findViewById(R.id.searchView);

        // Initialize the location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Get the map fragment and set up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Handle search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchForLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            getCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        // Ensure permission is still granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location location = task.getResult();
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        // Add a marker at the current location and move the camera
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    } else {
                        Toast.makeText(MapActivity2.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void searchForLocation(String query) {
        // Use Google Places API for searching locations
        FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountry("SA")  // Saudi Arabia country for localized search
                .build();

        placesClient.findAutocompletePredictions(predictionsRequest)
                .addOnSuccessListener(response -> {
                    // If we get a response with predictions, handle it here
                    if (response.getAutocompletePredictions().size() > 0) {
                        // Use the first prediction
                        String placeId = response.getAutocompletePredictions().get(0).getPlaceId();
                        fetchPlaceDetails(placeId);
                    } else {
                        // If no predictions, show a message
                        Toast.makeText(MapActivity2.this, "No locations found for this search", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Only show error if something fails at the API level
                    Toast.makeText(MapActivity2.this, "Error in search: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void fetchPlaceDetails(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME);
        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    Place place = response.getPlace();
                    LatLng placeLocation = place.getLatLng();

                    if (placeLocation != null) {
                        mMap.addMarker(new MarkerOptions().position(placeLocation).title(place.getName()));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 15));
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MapActivity2.this, "Error fetching place details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission is required to show your current location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
