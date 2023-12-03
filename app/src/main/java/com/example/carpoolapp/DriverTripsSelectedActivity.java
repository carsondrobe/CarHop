package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DriverTripsSelectedActivity extends AppCompatActivity {

    // Create variables
    Button goBack;
    Button confirmTrips;
    TextView trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trips_selected);

        // get values from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int numPassengers = bundle.getInt("numPassengers");
        String pickup = bundle.getString("pickup");
        String destination = bundle.getString("destination");
        String dateTime = bundle.getString("dateTime");

        // initialize and populate the trips text
        trips = findViewById(R.id.driver_trips_selected_tv_Trip1);

        trips.setText("From: " + pickup +
                        "\nTo: " + destination +
                        "\nNumber of Passengers: " + numPassengers +
                        "\nDate & Time: " + dateTime);

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
                Intent intent = new Intent(DriverTripsSelectedActivity.this, DriverMapActivity.class);
                startActivity(intent);
            }
        });
    }
}