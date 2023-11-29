package com.example.carpoolapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PassengerBookRideActivity extends AppCompatActivity {
    private ImageView backArrow;
    private Spinner spinnerNumPassengers;
    private int numPassengers;
    private RadioGroup radioGroupWhen;
    private RadioButton radioButtonReserve;
    private boolean reserve = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_book_ride);

        // Initialize UI components
        initializeViews();

        // Initialize Places API
        initializePlacesApi();

        // Set back arrow click listener
        setBackArrowClickListener();

        // Set listener on numPassengers Spinner
        setNumPassengersSpinnerListener();

        // Set listener for reserve RadioButton
        setReserveRadioButtonListener();
    }

    private void initializeViews() {
        backArrow = findViewById(R.id.activity_passenger_book_ride_ic_backarrow);
        spinnerNumPassengers = findViewById(R.id.activity_passenger_book_ride_spin_passenger_count);
        radioGroupWhen = findViewById(R.id.activity_passenger_book_ride_rgrp_when);
        radioButtonReserve = findViewById(R.id.activity_passenger_book_ride_rbtn_reserve);
    }

    private void initializePlacesApi() {
        final String apiKey = BuildConfig.apikey;
        // Initialize Places API
        Places.initialize(getApplicationContext(), apiKey);

        // Initialize AutocompleteSupportFragments for pickup and destination
        initializeAutocompleteFragment(R.id.activity_passenger_book_ride_frag_pickup, "Pickup Location");
        initializeAutocompleteFragment(R.id.activity_passenger_book_ride_frag_destination, "Destination Location");
    }

    private void initializeAutocompleteFragment(int fragmentId, String hint) {
        AutocompleteSupportFragment autocompleteFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(fragmentId);

        // Set hint for the AutocompleteSupportFragment
        autocompleteFragment.setHint(hint);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void setBackArrowClickListener() {
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to passenger home screen
                Intent intent = new Intent(PassengerBookRideActivity.this, PassengerMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setNumPassengersSpinnerListener() {
        spinnerNumPassengers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Update numPassengers variable to spinner value
                String selectedValue = parentView.getItemAtPosition(position).toString();
                try {
                    numPassengers = Integer.parseInt(selectedValue);
                } catch (NumberFormatException e) {
                    // Handle the exception (e.g., log, display an error message)
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // By default, assume the ride is only for 1 person
                numPassengers = 1;
            }
        });
    }

    private void setReserveRadioButtonListener() {
        radioButtonReserve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the 'reserve' variable based on the checked state
                reserve = isChecked;
            }
        });
    }
}