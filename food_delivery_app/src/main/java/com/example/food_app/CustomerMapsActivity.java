package com.example.food_app;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "CustomerMapActivity";

    private static final float DEFAULT_ZOOM = 17f;

    private com.google.android.gms.location.FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng myLocation;
    private double lat;
    private double lon;
    private Marker myMarker;
    private Button confirm;
    private String destination;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database = FirebaseDatabase.getInstance();

        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = "Customer";
                //FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = database.getReference("CustomerLocation");

                GeoFire geoFire = new GeoFire(ref);
                geoFire.setLocation(userID, new GeoLocation(lat,lon));

                Intent intent = new Intent(CustomerMapsActivity.this, KitchensListActivity.class);
                startActivity(intent);

            }
        });
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getDeviceLocation();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
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
                        final Location currentLocation = (Location) task.getResult();
                        lat = currentLocation.getLatitude();
                        lon = currentLocation.getLongitude();
                        myLocation = new LatLng(lat, lon);
                        mMap.clear();
                        moveCamera(myLocation,
                                DEFAULT_ZOOM);
                        myMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Delivery Location").draggable(true));

                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                myLocation = myMarker.getPosition();
                                lat = myLocation.latitude;
                                lon = myLocation.longitude;
                            }
                        });
                    } else {
                        Log.d(TAG, "onComplete: current location is null");
                        Toast.makeText(CustomerMapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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
}
