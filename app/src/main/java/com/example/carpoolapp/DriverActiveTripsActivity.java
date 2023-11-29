package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriverActiveTripsActivity extends AppCompatActivity {

    // Create variables
    Button cancelAllTrips;
    Button bookANewTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_active_trips);
        // Initialize buttons
        cancelAllTrips = findViewById(R.id.driver_active_trips_btn_cancelAllTrips);
        bookANewTrip = findViewById(R.id.driver_active_trips_btn_BookANewTrip);
        // Go to DriverMainActivity if cancelAllTrips button clicked
        cancelAllTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverActiveTripsActivity.this, DriverMainActivity.class);
                startActivity(intent);
            }
        });
        // Go to DriverBookTripActivity if bookANewTrip button clicked
        bookANewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverActiveTripsActivity.this, DriverBookTripActivity.class);
                startActivity(intent);
            }
        });
    }
}