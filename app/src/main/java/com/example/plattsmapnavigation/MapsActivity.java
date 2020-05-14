package com.example.plattsmapnavigation;

import android.Manifest;
import android.app.Activity;
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

/* TODO: -have info window pop up right as marker is placed*/

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        RoutingListener,
        GoogleMap.OnPolylineClickListener {

    private static final int MY_PERMISSION_FINE_LOCATION = 101;
    private static final String google_maps_api_key = "AIzaSyAifXCHf536CDtqHh6Qge2QYcTPvNp5BBU";
    private static final String locationSnippet = "Tap Here For Directions";
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    Double myLongitude = null;
    Double myLatitude = null;
    LatLng myLocation = null;
    LatLng endRoute = null;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private List<Polyline> polylinesList;
    private ArrayList<Integer> routeDurations;

    public static int addParking=0;
    public static int addLectureHall=0;
    public static int addResidenceHall=0;
    public static int addDiningHall=0;
    public static int addServiceArea=0;
    static final int REQUEST_CODE=0;

    public static int itemselector=0;



    private static final LatLng ParkingWhiteface = new LatLng(44.6928, -73.4688);
    private static final LatLng ParkingAusable = new LatLng(44.6969, -73.4686);

    private static final LatLng LectureHallAusable = new LatLng(44.6975, -73.4686);
    private static final LatLng LectureHallHawkins = new LatLng(44.6970, -73.4674);
    private static final LatLng LectureHallWard = new LatLng(44.6965, -73.4686);
    private static final LatLng LectureHallHudson = new LatLng(44.6957, -73.4674);
    private static final LatLng LectureHallMemorial = new LatLng(44.693110, -73.464724);
    private static final LatLng LectureHallSibley = new LatLng(44.688763, -73.469939);
    private static final LatLng LectureHallRedcay = new LatLng(44.696710, -73.465518);
    private static final LatLng LectureHallYokum = new LatLng(44.694285, -73.466997);
    private static final LatLng LectureHallBeaumont = new LatLng(44.695093, -73.467512);
    private static final LatLng LectureHallChamplain = new LatLng(44.691874, -73.467834);
    private static final LatLng LectureHallSaranac = new LatLng(44.692256, -73.465816);

    private static final LatLng ResidenceHallWhiteface = new LatLng(44.6916, -73.4680);
    private static final LatLng ResidenceHallKent = new LatLng(44.6909, -73.4670);
    private static final LatLng ResidenceHallMacomb = new LatLng(44.69113, -73.4668);
    private static final LatLng ResidenceHallHarrington = new LatLng(44.693629, -73.463586);
    private static final LatLng ResidenceHallMacdonough = new LatLng(44.693796, -73.462512);
    private static final LatLng ResidenceHallMason = new LatLng(44.690502, -73.467837);
    private static final LatLng ResidenceHallHood = new LatLng(44.690517, -73.468396);
    private static final LatLng ResidenceHalldeFredenburgh = new LatLng(44.690059, -73.468567);
    private static final LatLng ResidenceHallMoffitt = new LatLng(44.689510, -73.468353);
    private static final LatLng ResidenceHallWilson = new LatLng(44.689388, -73.467751);
    private static final LatLng ResidenceHallAdirondack = new LatLng(44.691356, -73.469018);
    private static final LatLng ResidenceHallBanks = new LatLng(44.692103, -73.468932);

    private static final LatLng DiningHallClinton = new LatLng(44.68981, -73.46813);
    private static final LatLng DiningHallAlgonquin = new LatLng(44.691356, -73.468396);

    private static final LatLng ServiceAreaKehoe = new LatLng(44.6943, -73.46622);
    private static final LatLng ServiceAreaPoliceHealth = new LatLng(44.691707, -73.465561);
    private static final LatLng ServiceAreaAngel = new LatLng(44.692713, -73.466463);
    private static final LatLng ServiceAreaLibrary = new LatLng(44.693476, -73.467362);
    private static final LatLng ServiceAreaArt = new LatLng(44.693568, -73.465966);



    private Marker mParkingWhiteface;
    private Marker mParkingAusable;
    private Marker mLectureHallAusable;
    private Marker mLectureHallChamplain;
    private Marker mLectureHallSaranac;
    private Marker mLectureHallYokum;
    private Marker mLectureHallBeaumont;
    private Marker mLectureHallMemorial;
    private Marker mLectureHallSibley;
    private Marker mLectureHallRedcay;
    private Marker mLectureHallWard;
    private Marker mLectureHallHawkins;
    private Marker mLectureHallHudson;
    private Marker mResidenceHallWhiteface;
    private Marker mResidenceHallKent;
    private Marker mResidenceHallMacomb;
    private Marker mDiningHallClinton;
    private Marker mDiningHallAlgonquin;
    private Marker mServiceAreaKehoe;
    private Marker mServiceAreaAngel;
    private Marker mServiceAreaArt;
    private Marker mServiceAreaLibrary;
    private Marker mServiceAreaPoliceHealth;
    private Marker mResidenceHallHarrington;
    private Marker mResidenceHallMacdonough;
    private Marker mResidenceHallMason;
    private Marker mResidenceHallHood;
    private Marker mResidenceHalldeFredenburgh;
    private Marker mResidenceHallMoffitt;
    private Marker mResidenceHallWilson;
    private Marker mResidenceHallAdirondack;
    private Marker mResidenceHallBanks;





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
        String[] items = new String[]{"GO TO...","Home", "Edit Schedule", "View Schedule", "Mark Parking","Mark Lecture Halls", "Mark Residence Halls", "Mark Dining Halls", "Mark Service Areas"};
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
                        addServiceArea=0;
                        addDiningHall=0;
                        itemselector=0;
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
                            addServiceArea=0;
                            addDiningHall=0;
                            itemselector=0;
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
                            addServiceArea=0;
                            addDiningHall=0;
                            itemselector=0;
                            startActivity(intent1);
                        }

                    case "Mark Parking":
                        addParking += 1;
                        addLectureHall=0;
                        addResidenceHall=0;
                        addServiceArea=0;
                        addDiningHall=0;
                        itemselector=0;
                        Intent intent2 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent2);
                        break;

                    case "Mark Lecture Halls":
                        addLectureHall += 1;
                        addParking=0;
                        addResidenceHall=0;
                        addServiceArea=0;
                        addDiningHall=0;
                        itemselector=0;
                        Intent intent3 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent3);
                        break;

                    case "Mark Residence Halls":
                        addResidenceHall += 1;
                        addParking=0;
                        addLectureHall=0;
                        addServiceArea=0;
                        addDiningHall=0;
                        itemselector=0;
                        Intent intent4 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent4);
                        break;

                    case "Mark Service Areas":
                        addServiceArea += 1;
                        addParking=0;
                        addLectureHall=0;
                        addResidenceHall=0;
                        addDiningHall=0;
                        itemselector=0;
                        Intent intent5 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent5);
                        break;

                    case "Mark Dining Halls":
                        addDiningHall += 1;
                        addParking=0;
                        addLectureHall=0;
                        addResidenceHall=0;
                        addServiceArea=0;
                        itemselector =0;
                        Intent intent6 = new Intent(MapsActivity.this,MapsActivity.class);
                        startActivity(intent6);
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
            addServiceArea=0;
            addDiningHall=0;
            itemselector=0;
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
        // Create a new instance of Intent to start DetailActivity
        itemselector=0;
        addLectureHall=0;
        addServiceArea=0;
        addDiningHall=0;
        addResidenceHall=0;
        addParking=0;
        final Intent intent = new Intent(this, MapSearch.class);

        // Start DetailActivity with the request code
        startActivityForResult(intent, REQUEST_CODE);

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

        Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.drawable. dining);
        Bitmap diningHallMarker = Bitmap.createScaledBitmap(b3, markerWidth, markerHeight, false);
        BitmapDescriptor smallDiningIcon = BitmapDescriptorFactory.fromBitmap(diningHallMarker);

        Bitmap b4 = BitmapFactory.decodeResource(getResources(), R.drawable. service);
        Bitmap serviceAreaMarker = Bitmap.createScaledBitmap(b4, markerWidth, markerHeight, false);
        BitmapDescriptor smallServiceIcon = BitmapDescriptorFactory.fromBitmap(serviceAreaMarker);




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

            switch(itemselector){
                case 0:
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

                    mLectureHallHudson = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallHudson)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Hudson Hall + Annex"));

                    mLectureHallHudson.setTag(0);

                    mLectureHallWard = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallWard)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Ward Hall"));
                    mLectureHallWard.setTag(0);

                    mLectureHallMemorial = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallMemorial)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Memorial Hall"));
                    mLectureHallMemorial.setTag(0);

                    mLectureHallRedcay = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallRedcay)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Redcay Hall"));
                    mLectureHallRedcay.setTag(0);

                    mLectureHallSibley = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallSibley)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Sibley Hall"));
                    mLectureHallSibley.setTag(0);

                    mLectureHallYokum = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallYokum)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Yokum Hall"));
                    mLectureHallYokum.setTag(0);

                    mLectureHallBeaumont = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallBeaumont)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Beaumont Hall"));
                    mLectureHallBeaumont.setTag(0);

                    mLectureHallChamplain = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallChamplain)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Champlain Valley Hall"));
                    mLectureHallChamplain.setTag(0);
                    break;

                case 1:
                    mLectureHallAusable = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallAusable)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .title("Ausable Hall")
                            .snippet(locationSnippet));
                    mLectureHallAusable.setTag(0);
                    break;

                case 2:
                    mLectureHallBeaumont = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallBeaumont)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Beaumont Hall"));
                    mLectureHallBeaumont.setTag(0);
                    break;

                case 3:
                    mLectureHallChamplain = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallChamplain)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Champlain Valley Hall"));
                    mLectureHallChamplain.setTag(0);
                    break;

                case 4:
                    mLectureHallHawkins = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallHawkins)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .title("Hawkins Hall")
                            .snippet(locationSnippet));
                    mLectureHallHawkins.setTag(0);
                    break;

                case 5:
                    mLectureHallHudson = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallHudson)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Hudson Hall + Annex"));
                    break;

                case 6:
                    mLectureHallMemorial = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallMemorial)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Memorial Hall"));
                    break;
                case 7:
                    mLectureHallRedcay = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallRedcay)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Redcay Hall"));
                    break;

                case 8:
                    mLectureHallSibley = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallSibley)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Sibley Hall"));
                    mLectureHallSibley.setTag(0);
                    break;

                case 9:
                    mLectureHallWard = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallWard)
                            .icon(BitmapDescriptorFactory.fromBitmap(lectureHallMarker))
                            .snippet(locationSnippet)
                            .title("Ward Hall"));
                    break;



            }



        }

        if (addResidenceHall >=1){
            switch(itemselector) {
                case 0:
                    mResidenceHallWhiteface = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallWhiteface)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .title("Whiteface Hall")
                            .snippet(locationSnippet));
                    mResidenceHallWhiteface.setTag(0);

                    mResidenceHallKent = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallKent)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Kent Hall"));
                    mResidenceHallKent.setTag(0);

                    mResidenceHallMacomb = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMacomb)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Macomb Hall"));
                    mResidenceHallMacomb.setTag(0);

                    mResidenceHallHarrington = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallHarrington)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Harrington Hall"));
                    mResidenceHallHarrington.setTag(0);

                    mResidenceHallMacdonough = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMacdonough)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Macdonough Hall"));
                    mResidenceHallMacdonough.setTag(0);

                    mResidenceHallMason = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMason)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Mason Hall"));
                    mResidenceHallMason.setTag(0);

                    mResidenceHallHood = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallHood)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Hood Hall"));
                    mResidenceHallHood.setTag(0);

                    mResidenceHalldeFredenburgh = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHalldeFredenburgh)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("deFredenburgh Hall"));
                    mResidenceHalldeFredenburgh.setTag(0);

                    mResidenceHallMoffitt = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMoffitt)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Moffitt Hall"));
                    mResidenceHallMoffitt.setTag(0);

                    mResidenceHallWilson = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallWilson)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Wilson Hall"));
                    mResidenceHallWilson.setTag(0);

                    mResidenceHallAdirondack = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallAdirondack)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Adirondack Hall"));
                    mResidenceHallAdirondack.setTag(0);

                    mResidenceHallBanks = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallBanks)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Banks Hall"));
                    mResidenceHallBanks.setTag(0);
                    break;
                case 1:
                    mResidenceHallAdirondack = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallAdirondack)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Adirondack Hall"));
                    mResidenceHallAdirondack.setTag(0);
                    break;

                case 2:
                    mResidenceHallBanks = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallBanks)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Banks Hall"));
                    mResidenceHallBanks.setTag(0);
                    break;

                case 3:
                    mResidenceHalldeFredenburgh = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHalldeFredenburgh)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("deFredenburgh Hall"));
                    mResidenceHalldeFredenburgh.setTag(0);
                    break;

                case 4:
                    mResidenceHallHarrington = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallHarrington)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Harrington Hall"));
                    break;

                case 5:
                    mResidenceHallHood = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallHood)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Hood Hall"));
                    break;

                case 6:
                    mResidenceHallKent = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallKent)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Kent Hall"));
                    break;

                case 7:
                    mResidenceHallMacdonough = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMacdonough)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Macdonough Hall"));
                    break;

                case 8:
                    mResidenceHallMacomb = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMacomb)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Macomb Hall"));
                    break;

                case 9:
                    mResidenceHallMason = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMason)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Mason Hall"));
                    break;

                case 10:
                    mResidenceHallMoffitt = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallMoffitt)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Moffitt Hall"));
                    mResidenceHallMoffitt.setTag(0);
                    break;

                case 11:
                    mResidenceHallWhiteface = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallWhiteface)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .title("Whiteface Hall")
                            .snippet(locationSnippet));
                    mResidenceHallWhiteface.setTag(0);
                    break;

                case 12:
                    mResidenceHallWilson = mMap.addMarker(new MarkerOptions()
                            .position(ResidenceHallWilson)
                            .icon(BitmapDescriptorFactory.fromBitmap(residenceHallMarker))
                            .snippet(locationSnippet)
                            .title("Wilson Hall"));
                    mResidenceHallWilson.setTag(0);
                    break;
            }


        }

        if (addDiningHall >=1){
            switch(itemselector) {
                case 0:
                    mDiningHallClinton = mMap.addMarker(new MarkerOptions()
                            .position(DiningHallClinton)
                            .icon(BitmapDescriptorFactory.fromBitmap(diningHallMarker))
                            .snippet(locationSnippet)
                            .title("Clinton Dining Hall"));
                    mDiningHallClinton.setTag(0);

                    mDiningHallAlgonquin = mMap.addMarker(new MarkerOptions()
                            .position(DiningHallAlgonquin)
                            .icon(BitmapDescriptorFactory.fromBitmap(diningHallMarker))
                            .snippet(locationSnippet)
                            .title("Algonquin Dining Hall"));
                    mDiningHallAlgonquin.setTag(0);
                    break;

                case 1:
                    mDiningHallAlgonquin = mMap.addMarker(new MarkerOptions()
                            .position(DiningHallAlgonquin)
                            .icon(BitmapDescriptorFactory.fromBitmap(diningHallMarker))
                            .snippet(locationSnippet)
                            .title("Algonquin Dining Hall"));
                    mDiningHallAlgonquin.setTag(0);
                    break;

                case 2:
                    mDiningHallClinton = mMap.addMarker(new MarkerOptions()
                            .position(DiningHallClinton)
                            .icon(BitmapDescriptorFactory.fromBitmap(diningHallMarker))
                            .snippet(locationSnippet)
                            .title("Clinton Dining Hall"));
                    mDiningHallClinton.setTag(0);

                    break;

            }

        }



        if (addServiceArea >=1){
            switch(itemselector) {

                case 0:
                    mServiceAreaKehoe = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaKehoe)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Kehoe Administration Building"));
                    mServiceAreaKehoe.setTag(0);

                    mServiceAreaAngel = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaAngel)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Angell College Center"));
                    mServiceAreaAngel.setTag(0);

                    mServiceAreaPoliceHealth = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaPoliceHealth)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Health Center/University Police"));
                    mServiceAreaPoliceHealth.setTag(0);

                    mServiceAreaLibrary = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaLibrary)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Feinberg Library"));
                    mServiceAreaLibrary.setTag(0);

                    mServiceAreaArt = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaArt)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Myers Fine Arts Building"));
                    mServiceAreaArt.setTag(0);

                    mLectureHallSaranac = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallSaranac)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Saranac Hall"));
                    mLectureHallSaranac.setTag(0);

                    break;
                case 1:
                    mServiceAreaAngel = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaAngel)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Angell College Center"));
                    mServiceAreaAngel.setTag(0);

                    break;

                case 2:
                    mServiceAreaPoliceHealth = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaPoliceHealth)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Health Center/University Police"));
                    mServiceAreaPoliceHealth.setTag(0);

                    break;

                case 3:
                    mServiceAreaLibrary = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaLibrary)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Feinberg Library"));
                    mServiceAreaLibrary.setTag(0);
                    break;

                case 4:
                    mServiceAreaKehoe = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaKehoe)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Kehoe Administration Building"));
                    mServiceAreaKehoe.setTag(0);
                    break;

                case 5:
                    mServiceAreaArt = mMap.addMarker(new MarkerOptions()
                            .position(ServiceAreaArt)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Myers Fine Arts Building"));
                    mServiceAreaArt.setTag(0);
                    break;

                case 6:
                    mLectureHallSaranac = mMap.addMarker(new MarkerOptions()
                            .position(LectureHallSaranac)
                            .icon(BitmapDescriptorFactory.fromBitmap(serviceAreaMarker))
                            .snippet(locationSnippet)
                            .title("Saranac Hall"));
                    mLectureHallSaranac.setTag(0);

                    break;

            }
        }




        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnPolylineClickListener(this);
        LatLng one = new LatLng(44.6960, -73.4669);
        LatLng two = new LatLng(44.687, -73.46);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == REQUEST_CODE) {


            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                final String result = data.getStringExtra(MapSearch.EXTRA_DATA);
                Toast.makeText(MapsActivity.this, result,
                        Toast.LENGTH_LONG).show();

                switch(result){
                    case "Ausable Hall":
                        itemselector =1;
                        addLectureHall+=1;
                        break;
                    case "Beaumont Hall":
                        itemselector =2;
                        addLectureHall+=1;
                        break;
                    case "Champlain Valley Hall":
                        itemselector =3;
                        addLectureHall+=1;
                        break;

                    case "Hawkins Hall":
                        itemselector =4;
                        addLectureHall+=1;
                        break;

                    case "Hudson Hall":
                        itemselector =5;
                        addLectureHall+=1;
                        break;

                    case "Memorial Hall":
                        itemselector =6;
                        addLectureHall+=1;
                        break;

                    case "Redcay Hall":
                        itemselector =7;
                        addLectureHall+=1;
                        break;

                    case "Sibley Hall":
                        itemselector =8;
                        addLectureHall+=1;
                        break;

                    case "Ward Hall":
                        itemselector =9;
                        addLectureHall+=1;
                        break;

                    case "Algonquin Dining Hall" :
                        itemselector=1;
                        addDiningHall+=1;
                        break;

                    case "Clinton Dining Hall" :
                        itemselector=2;
                        addDiningHall+=1;
                        break;

                    case "Angell College Center":
                        itemselector=1;
                        addServiceArea+=1;
                        break;

                    case "Campus Police and Health":
                        itemselector=2;
                        addServiceArea+=1;
                        break;

                    case "Feinberg Library":
                        itemselector=3;
                        addServiceArea+=1;
                        break;

                    case "Kehoe Administration Office":
                        itemselector=4;
                        addServiceArea+=1;
                        break;

                    case "Myers Fine Arts Building":
                        itemselector=5;
                        addServiceArea+=1;
                        break;

                    case "Saranac Hall":
                        itemselector=6;
                        addServiceArea+=1;
                        break;

                    case "Adirondack Hall":
                        itemselector=1;
                        addResidenceHall+=1;
                        break;

                    case "Banks Hall":
                        itemselector=2;
                        addResidenceHall+=1;
                        break;
                    case "deFredenburgh Hall":
                        itemselector=3;
                        addResidenceHall+=1;
                        break;
                    case "Harrington Hall":
                        itemselector=4;
                        addResidenceHall+=1;
                        break;
                    case "Hood Hall":
                        itemselector=5;
                        addResidenceHall+=1;
                        break;
                    case "Kent Hall":
                        itemselector=6;
                        addResidenceHall+=1;
                        break;
                    case "Macdonough Hall":
                        itemselector=7;
                        addResidenceHall+=1;
                        break;
                    case "Macomb Hall":
                        itemselector=8;
                        addResidenceHall+=1;
                        break;
                    case "Mason Hall":
                        itemselector=9;
                        addResidenceHall+=1;
                        break;
                    case "Moffitt Hall":
                        itemselector=10;
                        addResidenceHall+=1;
                        break;
                    case "Whiteface Hall":
                        itemselector=11;
                        addResidenceHall+=1;
                        break;
                    case "Wilson Hall":
                        itemselector=12;
                        addResidenceHall+=1;
                        break;

                }
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

                }

            } else {

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
