package com.example.plattsmapnavigation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        RoutingListener,
        GoogleMap.OnPolylineClickListener {

    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    private static final String google_maps_api_key = "AIzaSyAifXCHf536CDtqHh6Qge2QYcTPvNp5BBU";
    private static final String locationSnippet = "Tap Here For Directions";
    private static final String TAG = "MapsActivity";
    private static GoogleMap mMap;
    Double myLongitude = null;
    Double myLatitude = null;
    LatLng myLocation = null;
    LatLng endRoute = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<Polyline> polylinesList;
    private ArrayList<Integer> routeDurations;

    public static boolean next_class = false;

    public static int addParking=0;
    public static int addLectureHall=0;
    public static int addResidenceHall=0;



    private static final LatLng ParkingWhiteface = new LatLng(44.6928, -73.4688);
    private static final LatLng ParkingAusable = new LatLng(44.6969, -73.4686);

    private static final LatLng LectureHallAusable = new LatLng(44.6975, -73.4686);
    private static final LatLng LectureHallHawkins = new LatLng(44.6970, -73.4674);
    private static final LatLng LectureHallRedcay = new LatLng(44.696764699999996,-73.4655659);

    private static final LatLng ResidenceHallWhiteface = new LatLng(44.6916, -73.4680);








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
        fixGoogleMapBug();

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        String[] items = new String[]{"GO TO...","Home", "Edit Schedule", "View Schedule", "Mark Parking","Mark Lecture Halls", "Mark Residence Halls"};
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
                        addParking =0;
                        addLectureHall=0;
                        addResidenceHall=0;
                        Intent intent = new Intent(MapsActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;

                    case "Edit Schedule":

                        if (SignInStatus.SignedIn == false){
                            Toast.makeText(MapsActivity.this, "You must be signed in to edit schedule.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent l = new Intent(MapsActivity.this,ScheduleActivity.class);
                            addParking= 0;
                            addLectureHall=0;
                            addResidenceHall=0;
                            startActivity(l);
                        }
                    case "View Schedule":

                        if (SignInStatus.SignedIn == false){
                            Toast.makeText(MapsActivity.this, "You must be signed in to view schedule.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Intent intent1 = new Intent(MapsActivity.this, InputScheduleActivity.class);
                            addParking=0;
                            addLectureHall=0;
                            addResidenceHall=0;
                            startActivity(intent1);
                        }

                    case "Mark Parking":
                        addParking += 1;
                        Intent intent2 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent2);
                        break;

                    case "Mark Lecture Halls":
                        addLectureHall += 1;
                        Intent intent3 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent3);
                        break;

                    case "Mark Residence Halls":
                        addResidenceHall += 1;
                        Intent intent4 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent4);
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
            Toast.makeText(this, "Map reset", Toast.LENGTH_SHORT).show();
            addParking = 0;
            addLectureHall =0;
            addResidenceHall =0;
            resetMap();
        });
    }

    private void fixGoogleMapBug() {
        SharedPreferences googleBug = getSharedPreferences("google_bug", Context.MODE_PRIVATE);
        if (!googleBug.contains("fixed")) {
            File corruptedZoomTables = new File(getFilesDir(), "ZoomTables.data");
            corruptedZoomTables.delete();
            googleBug.edit().putBoolean("fixed", true).apply();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    public void onMapSearch(View view){
        EditText locationSearch = findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(location, 1);

        } catch (Exception e) {
            Toast.makeText(this, "Please Input A Location", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        if( addressList != null) {
            Address address = addressList.get(0);
            LatLng searchedLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            Marker marker = mMap.addMarker(new MarkerOptions().position(searchedLatLng).title(location).snippet(locationSnippet));
            marker.showInfoWindow();
            Log.d(TAG, String.valueOf(searchedLatLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(searchedLatLng));
        }
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

        Marker mParkingWhiteface;
        Marker mParkingAusable;
        Marker mLectureHallAusable;
        Marker mLectureHallHawkins;
        Marker mResidenceHallWhiteface;

        if(next_class != false) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .title(MainActivity.title)
                    .position(MainActivity.coordinates)
                    .snippet(locationSnippet)
            );
            marker.showInfoWindow();
            next_class = false;
        }

        /*

         */

        int markerHeight = 80;
        int markerWidth = 80;
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.parking);
        Bitmap parkingMarker = Bitmap.createScaledBitmap(b, markerWidth, markerHeight, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(parkingMarker);

        Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.lecture);
        Bitmap lectureHallMarker = Bitmap.createScaledBitmap(b1, markerWidth, markerHeight, false);
        BitmapDescriptor smallLectureIcon = BitmapDescriptorFactory.fromBitmap(lectureHallMarker);

        Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.residence);
        Bitmap residenceHallMarker = Bitmap.createScaledBitmap(b2, markerWidth, markerHeight, false);
        BitmapDescriptor smallResidenceIcon = BitmapDescriptorFactory.fromBitmap(residenceHallMarker);




        if (addParking >=1) {
            mParkingWhiteface = mMap.addMarker(new MarkerOptions()
                    .position(ParkingWhiteface)
                    .icon(BitmapDescriptorFactory.fromBitmap(parkingMarker))
                    .title("Off Campus Parking")
                    .snippet(locationSnippet));
            mParkingWhiteface.setTag(0);

            mParkingAusable = mMap.addMarker(new MarkerOptions()
                    .position(ParkingAusable)
                    .icon(BitmapDescriptorFactory.fromBitmap(parkingMarker))
                    .title("Off Campus Parking")
                    .snippet(locationSnippet));
            mParkingAusable.setTag(0);

        }

        if (addLectureHall >=1){
            mLectureHallAusable = mMap.addMarker(new MarkerOptions()
                    .position(LectureHallAusable)
                    .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                    .title("Ausable Hall")
                    .snippet(locationSnippet));
            mLectureHallAusable.setTag(0);

            mLectureHallHawkins = mMap.addMarker(new MarkerOptions()
                    .position(LectureHallHawkins)
                    .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                    .title("Hawkins Hall")
                    .snippet(locationSnippet));
            mLectureHallHawkins.setTag(0);


        }

        if (addResidenceHall >=1){
            mResidenceHallWhiteface = mMap.addMarker(new MarkerOptions()
                    .position(ResidenceHallWhiteface)
                    .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                    .title("Whiteface Hall")
                    .snippet(locationSnippet));
            mResidenceHallWhiteface.setTag(0);
        }




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
        endRoute = marker.getPosition();
        if ( marker.getSnippet().equals(locationSnippet) ) {
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
        } else {
            marker.hideInfoWindow();
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
        routeDurations = new ArrayList<>();

        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.grey1));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polyline.setClickable(true);
            polylinesList.add(polyline);
            Log.d(TAG, "Duration: " + route.get(i).getDurationValue());

            //adds trip durations to a global list
            routeDurations.add(route.get(i).getDurationValue() / 60);
        }

        int shortest_time = routeDurations.get(0);
        int shortest_time_index = 0;
        for (int i = 0; i < routeDurations.size(); i++) {
            if (routeDurations.get(i) < shortest_time) {
                shortest_time = i;
                shortest_time_index = i;
            }
        }
        onPolylineClick(polylinesList.get(shortest_time_index));


    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        int index = 0;  //used to display which route is selected
        for (Polyline polylineSelect : polylinesList) {
            index ++;
            if (polyline.getId().equals( polylineSelect.getId())) {
                polylineSelect.setColor(ContextCompat.getColor(this, R.color.blue1));
                polylineSelect.setZIndex(1);

                int duration = routeDurations.get(index - 1);

                Marker endTrip = mMap.addMarker(new MarkerOptions()
                        .position(endRoute)
                        .title("Route #" + index)
                        .snippet("Duration:  " + duration + " mins")
                );
                endTrip.showInfoWindow();
            } else {
                polylineSelect.setColor(ContextCompat.getColor(this, R.color.grey1));
                polylineSelect.setZIndex(0);            }
        }
    }

    public static void setMarker(LatLng latlng, String title) throws NullPointerException{
        try {
            //Log.d(TAG, "adding marker to map ###############################################");
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .title(title)
                    .position(latlng)
                    .snippet(locationSnippet)
            );
            marker.showInfoWindow();
            //Log.d(TAG, "marker should be on the map ##############################################");
        } catch (NullPointerException e) {
            //Log.d(TAG, e + "#########################################################");
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
