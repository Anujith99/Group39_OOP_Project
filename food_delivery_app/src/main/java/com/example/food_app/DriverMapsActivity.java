package com.example.food_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback,AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;

    private static final String TAG = "DriverMapsActivity";

    private static final float DEFAULT_ZOOM = 15f;

    private com.google.android.gms.location.FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng myLocation;
    private double latitude;
    private double longitude;
    private Marker myMarker;
    private int radius;
    private Spinner mSpinner;
    private Circle mapCircle;
    private Button confirm;

    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance();

        mSpinner = findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.delivery_radius,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);


        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = "Driver";
                //FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = database.getReference().child("Driver Availability");

                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(userID, new GeoLocation(latitude,longitude));
                ref.child(userID).child("radius").setValue(Integer.toString(radius));

                Intent intent = new Intent(DriverMapsActivity.this, KitchensListActivity.class);
                startActivity(intent);

            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting device's current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            final Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: found location!");
                        Location currentLocation = (Location) task.getResult();
                        latitude = currentLocation.getLatitude();
                        longitude = currentLocation.getLongitude();
                        myLocation = new LatLng(latitude, longitude);
                        mMap.clear();
                        moveCamera(myLocation,
                                DEFAULT_ZOOM);
                        myMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Delivery Location").draggable(true));

                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(DriverMapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        if(text.equals("Choose Radius")){

        }else{
            radius = Integer.parseInt(text);
            drawDeliveryRadius(radius);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void drawDeliveryRadius(int rad){
        if(mapCircle == null){
            mapCircle =mMap.addCircle(new CircleOptions()
                    .center(myLocation)
                    .radius(rad*1000)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f)
            );
        }else{
            mapCircle.remove();
            mapCircle =mMap.addCircle(new CircleOptions()
                    .center(myLocation)
                    .radius(rad*1000)
                    .strokeColor(Color.BLUE)
                    .fillColor(0x220000FF)
                    .strokeWidth(5.0f)
            );
        }

        moveCamera(myLocation, DEFAULT_ZOOM-rad+1);

    }
}
