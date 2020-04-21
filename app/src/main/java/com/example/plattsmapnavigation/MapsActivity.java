package com.example.plattsmapnavigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import android.content.DialogInterface;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        RoutingListener,
        GoogleMap.OnPolylineClickListener {

    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    private static final String google_maps_api_key = "AIzaSyAa0tDqcRDBZC40QHjQcIbXglBc9E_JL_8";
    //private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    Double myLongitude = null;
    Double myLatitude = null;
    LatLng myLocation = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<Polyline> polylinesList;



    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        polylinesList = new ArrayList<>();

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"GO TO...","Home", "Edit Schedule", "View Schedule"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);


        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String str = (String) arg0.getSelectedItem();

                switch(str){
                    case "Home":
                        Intent intent = new Intent(MapsActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;

                    case "Edit Schedule":

                    case "View Schedule":
                        Intent intent1 = new Intent(MapsActivity.this,InputScheduleActivity.class);
                        startActivity(intent1);
                        break;


                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        locationRequest = new LocationRequest();

        //how long it takes for location to update, quicker updates require more battery
        locationRequest.setInterval(15000);

        //how long it takes for location to update if an app is running location services in the background
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    //Update UI with location data
                    if (location != null) {
                        myLatitude = location.getLatitude();
                        myLongitude = location.getLongitude();
                    }
                }
                myLocation = new LatLng(myLatitude, myLongitude);
            }
        };

        ImageButton resetMap = findViewById(R.id.reset_map);
        resetMap.setOnClickListener(v -> {
            Toast.makeText(this, "Map has been reset", Toast.LENGTH_SHORT).show();
            resetMap();
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    public void onMapSearch(View view) {
        EditText locationSearch = findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        String snippet = "Tap here for directions to ";
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
        assert addressList != null;
        Address address = addressList.get(0);
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
        mMap.addMarker(new MarkerOptions().position(latLng).title(location).snippet(snippet + location));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "My Location Disabled.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "To Enable Location, Restart App and Enable Location", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnPolylineClickListener(this);
        LatLng one = new LatLng(44.6960, -73.4669);
        LatLng two = new LatLng(44.6920, -73.46);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(one);
        builder.include(two);

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        int padding = (int) (width * 0.20);
        mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding));
        mMap.setMinZoomPreference(mMap.getCameraPosition().zoom);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        //Toast.makeText(this, "Location Tapped", Toast.LENGTH_SHORT).show();

        if ( marker.getTitle().equals("My Location") ) {
            marker.hideInfoWindow();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Show Directions for: ")
                    .setCancelable(true)
                    .setPositiveButton("Driving", (dialog, which) -> {
                        calculateDirectionsDriving(marker);
                        dialog.dismiss();
                    })
                   .setNeutralButton("Walking", (dialog, which) -> {
                       calculateDirectionsWalking(marker);
                       dialog.dismiss();
                   })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void calculateDirectionsDriving(Marker marker) {
        LatLng mDestination = marker.getPosition();
        Routing routing = new Routing.Builder()
                .key(google_maps_api_key)
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(myLocation, mDestination)
                .build();
        routing.execute();
    }

    private void calculateDirectionsWalking(Marker marker) {
        LatLng mDestination = marker.getPosition();
        Routing routing = new Routing.Builder()
                .key(google_maps_api_key)
                .travelMode(AbstractRouting.TravelMode.WALKING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(myLocation, mDestination)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) throws NullPointerException{
        resetMap();
        if (polylinesList.size() > 0) {
            for (Polyline poly : polylinesList) {
                poly.remove();
            }
        }

        polylinesList = new ArrayList<>();

        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.grey1));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polyline.setClickable(true);
            polylinesList.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": duration -> " + (route.get(i).getDurationValue() % 60) + " mins", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

        for (Polyline polylineSelect : polylinesList) {
            if (polyline.getId().equals( polylineSelect.getId())) {
                polylineSelect.setColor(ContextCompat.getColor(this, R.color.blue1));
                polylineSelect.setZIndex(1);
            } else {
                polylineSelect.setColor(ContextCompat.getColor(this, R.color.grey1));
                polylineSelect.setZIndex(0);            }
        }
    }

    private void resetMap() {
        if (mMap != null) {
            mMap.clear();
        }

        if (polylinesList.size() > 0) {
            polylinesList.clear();
            polylinesList = new ArrayList<>();
        }
    }
}
