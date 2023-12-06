package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverTripsSelectedActivity extends AppCompatActivity {

    // Create variables
    Button goBack;
    Button confirmTrips;
    TextView trips;
    private DatabaseReference currentTripsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_trips_selected);

        currentTripsRef = FirebaseDatabase.getInstance().getReference().child("currentTrips");

        // get values from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int numPassengers = bundle.getInt("numPassengers");
        String pickup = bundle.getString("pickup");
        String destination = bundle.getString("destination");
        String dateTime = bundle.getString("dateTime");

        // create trip object
        Trip trip = new Trip (pickup, destination, dateTime, numPassengers);

        // initialize and populate the trips text
        trips = findViewById(R.id.selected_trips_text);

        trips.setText("From: " + pickup +
                        "\n\nTo: " + destination +
                        "\n\nNumber of Passengers: " + numPassengers +
                        "\n\nDate & Time: " + dateTime +
                        "\n\nIncome: + $5.00");

        // Initialize Buttons
        goBack = findViewById(R.id.driver_trips_selected_btn_goBack);
        confirmTrips = findViewById(R.id.driver_trips_selected_btn_confirmTrips);
        // Go to DriverActiveTripsActivity if confirmTrips button clicked
        confirmTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverTripsSelectedActivity.this, DriverMainActivity.class);
                currentTripsRef.push().setValue(trip)
                        .addOnSuccessListener(aVoid -> {
                            // Retrieve the key (database ID) of the new child
                            String recordKey = currentTripsRef.getKey();
                            intent.putExtra("recordKey", recordKey);
                            Toast.makeText(DriverTripsSelectedActivity.this, "Data written to Firebase", Toast.LENGTH_SHORT).show();

                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            String errorMessage = e.getMessage();
                            Log.e("Firebase", "Failed to write data: " + errorMessage);
                            Toast.makeText(DriverTripsSelectedActivity.this, "Failed to write data to Firebase", Toast.LENGTH_SHORT).show();
                        });
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