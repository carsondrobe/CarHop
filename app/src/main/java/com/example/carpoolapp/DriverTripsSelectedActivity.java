package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DriverTripsSelectedActivity extends AppCompatActivity {

    // Create variables
    Button goBack;
    Button confirmTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trips_selected);
        // Initialize Buttons
        goBack = findViewById(R.id.driver_trips_selected_btn_goBack);
        confirmTrips = findViewById(R.id.driver_trips_selected_btn_confirmTrips);
        // Go to DriverActiveTripsActivity if confirmTrips button clicked
        confirmTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverTripsSelectedActivity.this, DriverActiveTripsActivity.class);
                startActivity(intent);
            }
        });
        // Go to DriverBookTripActivity if goBack button clicked
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverTripsSelectedActivity.this, DriverBookTripActivity.class);
                startActivity(intent);
            }
        });
    }
}