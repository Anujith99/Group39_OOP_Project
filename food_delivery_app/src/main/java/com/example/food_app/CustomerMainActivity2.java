package com.example.food_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerMainActivity2 extends AppCompatActivity {

    Button customer_main_cart_btn;
    RecyclerView customer_main_recyclerView;
    private static final String TAG = "CustomerMainActivity";
    public ArrayList<String> mItems;
    public ArrayList<String> mPrices;

    static ArrayList<String[]> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main2);

        cart = new ArrayList<>();
        Resources res = getResources();
        customer_main_cart_btn = findViewById(R.id.customer_main_cart_btn);
        customer_main_recyclerView = findViewById(R.id.customer_main_recyclerView);

        mItems = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.items2)));
        mPrices = new ArrayList<>(Arrays.asList(res.getStringArray(R.array.prices2)));

        CustomerMainRecyclerViewAdapter adapter = new CustomerMainRecyclerViewAdapter(this,mItems,mPrices);
        customer_main_recyclerView.setAdapter(adapter);
        customer_main_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customer_main_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(),CustomerCartActivity2.class);
                startActivity(intent1);
            }
        });

    }
}
