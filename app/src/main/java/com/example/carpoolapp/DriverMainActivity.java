package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayout tripsContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        // Initialize Buttons
        backArrow = findViewById(R.id.driver_main_ic_BackArrow);
        bookTrip = findViewById(R.id.driver_main_btn_BookANewTrip);
        no_trips_selected = findViewById(R.id.driver_main_tv_NoTripsScheduled);
        tripsContainer = findViewById(R.id.trips_container);

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
                    intent = new Intent(DriverMainActivity.this, DriverMapActivity.class);
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
                tripsContainer.removeAllViews(); // Clear previous trip boxes

                if (!dataSnapshot.exists()) {
                    no_trips_selected.setVisibility(View.VISIBLE);
                    return;
                }

                no_trips_selected.setVisibility(View.GONE);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Trip trip = snapshot.getValue(Trip.class);
                    if (trip != null) {
                        CardView tripCard = new CardView(DriverMainActivity.this);
                        LinearLayout tripLayout = new LinearLayout(DriverMainActivity.this);
                        tripLayout.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutParams.setMargins(16, 16, 16, 16);
                        tripLayout.setLayoutParams(layoutParams);

                        TextView tripDetails = new TextView(DriverMainActivity.this);
                        tripDetails.setText("From: " + trip.getPickup() +
                                "\nTo: " + trip.getDestination() +
                                "\nNumber of Passengers: " + trip.getNumPassengers() +
                                "\nDate & Time: " + trip.getDateTime());
                        tripLayout.addView(tripDetails);

                        TextView tripCost = new TextView(DriverMainActivity.this);
                        tripCost.setText("+ $5.00");
                        tripCost.setTextSize(18); // Set text size (adjust as needed)
                        tripCost.setTypeface(null, Typeface.BOLD); // Set text style to bold
                        tripCost.setGravity(Gravity.CENTER);
                        tripLayout.addView(tripCost);

                        LinearLayout buttonLayout = new LinearLayout(DriverMainActivity.this);
                        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
                        buttonLayout.setGravity(Gravity.END); // Aligns children to the right

                        Button chatButton = new Button(DriverMainActivity.this);
                        LinearLayout.LayoutParams chatButtonParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        chatButton.setLayoutParams(chatButtonParams);
                        chatButton.setText("Chat");
                        chatButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DriverMainActivity.this, DriverChatActivity.class);
                                startActivity(intent);
                            }
                        });
                        buttonLayout.addView(chatButton);

                        Button removeButton = new Button(DriverMainActivity.this);
                        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        removeButton.setLayoutParams(buttonParams);
                        removeButton.setText("X");
                        removeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snapshot.getRef().removeValue();
                                tripsContainer.removeView(tripCard);
                            }
                        });
                        buttonLayout.addView(removeButton);

                        tripLayout.addView(buttonLayout);
                        tripCard.addView(tripLayout);
                        tripsContainer.addView(tripCard);
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