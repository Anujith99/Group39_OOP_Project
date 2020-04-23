package com.example.food_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class KitchensListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitchens_list);
    }

    public void items1(View view){
        Intent intent = new Intent(getApplicationContext(), CustomerMainActivity.class);
        startActivity(intent);
    }

    public void items2(View view){
        Intent intent = new Intent(getApplicationContext(), CustomerMainActivity2.class);
        startActivity(intent);
    }

    public void items3(View view){
        Intent intent = new Intent(getApplicationContext(), CustomerMainActivity3.class);
        startActivity(intent);
    }
}
