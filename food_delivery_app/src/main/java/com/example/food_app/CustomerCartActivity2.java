package com.example.food_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerCartActivity2 extends AppCompatActivity {
    static ArrayList<String[]> final_cart;
    ArrayList<String> mItemNames,mItemSizes,mItemPrices;
    RecyclerView cart_recyclerView;
    Button customer_cart_proceed_btn;
    TextView customer_cart_total_price, customer_cart_delivery_charges;
    double customer_lat,customer_long,home_lat,home_long;
    float[] results = new float[1];
    public float distance;
    public String order_id;

    RadioGroup radioGroup ;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cart2);

        final_cart = CustomerMainActivity.cart;
        mItemNames = new ArrayList<>();
        mItemSizes = new ArrayList<>();
        mItemPrices = new ArrayList<>();

        getItemData();
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        cart_recyclerView = findViewById(R.id.recyclerView);
        customer_cart_proceed_btn = findViewById(R.id.customer_cart_proceed_btn);
        customer_cart_total_price = findViewById(R.id.customer_cart_totalPrice_textView);
        customer_cart_delivery_charges = findViewById(R.id.customer_cart_totalDistance_textView);

        CustomerCartRecyclerViewAdapter adapter = new CustomerCartRecyclerViewAdapter(this,mItemNames,mItemSizes,mItemPrices);
        cart_recyclerView.setAdapter(adapter);
        cart_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toast.makeText(CustomerCartActivity2.this,"Choose Pricing Type To View Total", Toast.LENGTH_SHORT).show();

        customer_cart_proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").push();
                String number = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                String phonenumber = "+" + "91" + number;
                order_id = ref.getKey();
                Map<String,Object> map = new HashMap<>();
                map.put("id", order_id);
                map.put("home", "Rachel HS");
                map.put("phone", phonenumber);
                map.put("status", "placed");
                ref.setValue(map);
                customer_cart_proceed_btn.setEnabled(false);
            }
        });
    }

    public void getFinalPrice(View view){
        DatabaseReference cust_location = FirebaseDatabase.getInstance().getReference().child("CustomerLocation");
        DatabaseReference home_location = FirebaseDatabase.getInstance().getReference().child("HomeLocations");
        GeoFire cust_geofire = new GeoFire(cust_location);
        final GeoFire home_geofire = new GeoFire(home_location);

        cust_geofire.getLocation("Customer", new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                if(location!=null){
                    customer_lat = location.latitude;
                    customer_long = location.longitude;
                    home_geofire.getLocation("Home2", new LocationCallback() {
                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onLocationResult(String key, GeoLocation location) {
                            home_lat = location.latitude;
                            home_long = location.longitude;

                            Location.distanceBetween(customer_lat,customer_long,home_lat,home_long,results);

                            distance = Float.parseFloat(String.format("%.2f",results[0]/1000));
                            double per_km_price = Double.parseDouble(String.format("%.2f",distance*10));

                            String delivery = "Delivery Charges for " + distance + "km is Rs." + per_km_price;
                            customer_cart_delivery_charges.setText(delivery);

                            double total_price = 0.00;
                            for(String s:mItemPrices){
                                total_price = total_price+Double.parseDouble(s);
                            }

                            total_price = Double.parseDouble(String.format("%.2f",total_price + per_km_price));
                            int radioID = radioGroup.getCheckedRadioButtonId();
                            radioButton = findViewById(radioID);


                            if(radioButton.getText().equals("Regular(1 Week)")){
                                String regular_text = "Total Price: Rs." + String.format("%.2f",total_price*0.8);
                                customer_cart_total_price.setText(regular_text);

                            }else{
                                String occ_text = "Total Price: Rs." + total_price;
                                customer_cart_total_price.setText(occ_text);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void getItemData(){
        for(int i = 0;i<CustomerMainActivity2.cart.size();i++){
            String[] arr = CustomerMainActivity2.cart.get(i);
            mItemNames.add(i, arr[0]);
            mItemSizes.add(i, arr[1]);
            mItemPrices.add(i, arr[2]);
        }
    }

    public void checkStatus(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Orders").child(order_id);
        ref.child("status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                if(status.equals("accepted")){
                    Intent intent = new Intent(CustomerCartActivity2.this,CustomerOTPActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
