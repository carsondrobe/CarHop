package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationBarView;

public class DriverBookTripActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_book_trip);
        initializeNavBar();
        //TODO...

    }
    private void initializeNavBar(){
        NavigationBarView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.home:
                    intent = new Intent(DriverBookTripActivity.this, DriverMainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.book:
                    break;
                case R.id.finances:
                    intent = new Intent(DriverBookTripActivity.this, UsageSummary.class);
                    intent.putExtra("source", "driver");
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }
}