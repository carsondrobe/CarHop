package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DriverMainActivity extends AppCompatActivity {

    // Create variables
    ImageView backArrow;
    Button bookTrip;
    private ArrayList<Trip> trips;
    TextView trips_text, no_trips_selected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        // Initialize Buttons
        backArrow = findViewById(R.id.driver_main_ic_BackArrow);
        bookTrip = findViewById(R.id.driver_main_btn_BookANewTrip);
        trips_text = findViewById(R.id.trips_selected_main);
        no_trips_selected = findViewById(R.id.driver_main_tv_NoTripsScheduled);

        trips = new ArrayList<>();

        initializeNavBar();

        retrieveRecordsFromDatabase();

        // Back to Main Menu onClick back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverMainActivity.this, SelectRoleActivity.class);
                startActivity(intent);
            }
        });
        // Go to DriverBookTripActivity if Book Trip button clicked
        bookTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBookTripActivity();
            }
        });
    }

    // Launch Book Ride Activity Method for readability
    void startBookTripActivity(){
        Intent intent = new Intent(DriverMainActivity.this, DriverMapActivity.class);
        startActivity(intent);
    }

    private void initializeNavBar(){
        NavigationBarView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.home:
                    break;
                case R.id.book:
                    intent = new Intent(DriverMainActivity.this, DriverBookTripActivity.class);
                    startActivity(intent);
                    break;
                case R.id.finances:
                    intent = new Intent(DriverMainActivity.this, UsageSummaryActivity.class);
                    intent.putExtra("source", "driver");
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }
    private void retrieveRecordsFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("currentTrips");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(trips != null) {
                    trips.clear();
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Trip trip = snapshot.getValue(Trip.class);
                    if (trip != null) {
                        Trip record = new Trip(trip.getPickup(), trip.getDestination(), trip.getDateTime(), trip.getNumPassengers());
                        trips.add(record);

                        trips_text.setText("From: " + record.getPickup() +
                                "\n\nTo: " + record.getDestination() +
                                "\n\nNumber of Passengers: " + record.getNumPassengers() +
                                "\n\nDate & Time: " + record.getDateTime());
                        no_trips_selected.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverMainActivity.this, "Failed to retrieve records from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}