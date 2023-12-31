package com.example.carpoolapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PassengerBookRideActivity extends AppCompatActivity {
    private ImageView backArrow;
    private Spinner spinnerNumPassengers;
    private int numPassengers;
    private RadioGroup radioGroupWhen;
    private RadioButton radioButtonReserve;
    private RadioButton radioButtonNow;
    private boolean reserve = false;
    private Place pickup;
    private Place destination;
    private Button selectDriverButton;
    private String date;
    private String time;
    private TextView textViewSelectedDate;
    private DatabaseReference tripsRef;


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

        initializeNavBar();

        // When driver button onClick is pressed
        selectDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDriverOnClick();
            }
        });
    }

    private void initializeViews() {
        backArrow = findViewById(R.id.activity_passenger_book_ride_ic_backarrow);
        spinnerNumPassengers = findViewById(R.id.activity_passenger_book_ride_spin_passenger_count);
        radioGroupWhen = findViewById(R.id.activity_passenger_book_ride_rgrp_when);
        radioButtonReserve = findViewById(R.id.activity_passenger_book_ride_rbtn_reserve);
        selectDriverButton = findViewById(R.id.activity_passenger_book_ride_btn_select_driver);
        textViewSelectedDate = findViewById(R.id.activity_passenger_book_ride_tv_selectedDate);
        radioButtonNow = findViewById(R.id.activity_passenger_book_ride_rbtn_now);

        //initialize database
        tripsRef = FirebaseDatabase.getInstance().getReference().child("trips");
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
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        // Set a bias for Kelowna

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(49.8129, -119.6303), // Southwest corner of Kelowna
                new LatLng(49.9901, -119.3635))); // Northeast corner of Kelowna

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //Check for Pickup or Destination and update places
                if(fragmentId == R.id.activity_passenger_book_ride_frag_pickup){
                    Log.i(TAG, "Pickup: " + place.getName() + ", " + place.getId());
                    pickup = place;
                } else if (fragmentId == R.id.activity_passenger_book_ride_frag_destination) {
                    Log.i(TAG, "Destination: " + place.getName() + ", " + place.getId());
                    destination = place;
                }
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
        radioGroupWhen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.activity_passenger_book_ride_rbtn_reserve) {
                    reserve = true;
                    showDatePickerDialog();
                    showTimePickerDialog();
                    selectDriverButton.setText("Reserve");
                } else {
                    reserve = false;
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());

                    date = sdfDate.format(calendar.getTime());
                    time = sdfTime.format(calendar.getTime());
                    updateSelectedReservationText();
                    textViewSelectedDate.append(" (now)");
                }
            }
        });
        radioButtonNow.setChecked(true);
    }

    private void updateSelectedReservationText() {
        // Update the reservation text view
        if(date != null && time != null)
            textViewSelectedDate.setText(time + " " + date);
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the selected date
                        updateSelectedDate(selectedYear, selectedMonth, selectedDay);
                        updateSelectedReservationText();
                    }
                },
                year, month, dayOfMonth
        );
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void updateSelectedDate(int year, int month, int day) {
        // Format the selected date and update the variable
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(selectedDate.getTime());
        date = formattedDate;
    }
    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a TimePickerDialog and show it
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int selectedHourOfDay, int selectedMinute) {
                        // Handle the selected time
                        updateSelectedTime(selectedHourOfDay, selectedMinute);
                    }
                },
                hourOfDay, minute, true // true for 24-hour format
        );
        timePickerDialog.show();
    }

    private void updateSelectedTime(int hourOfDay, int minute) {
        // Format the selected time and update variable
        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
        time = formattedTime;
    }
    private void selectDriverOnClick(){
        // Check if all fields are valid
        if (pickup != null && destination != null && date != null && time != null) {
            // Create an Intent to start the SelectedDriverActivity
            Intent intent;
            if (reserve == false)
                intent = new Intent(PassengerBookRideActivity.this, PassengerSelectDriverActivity.class);
            else{
                intent = new Intent(PassengerBookRideActivity.this, PassengerAfterReservedMainActivity.class);
            }

            // Bundle the data and add it as extras to the Intent
            intent.putExtra("numPassengers", numPassengers);
            intent.putExtra("reserve", reserve);
            intent.putExtra("pickupPlaceName", pickup.getName());
            intent.putExtra("pickupPlaceId", pickup.getId());
            intent.putExtra("destinationPlaceName", destination.getName());
            intent.putExtra("destinationPlaceId", destination.getId());
            intent.putExtra("date", date);
            intent.putExtra("time", time);

            String dateTime = date + " " + time;

            //Write data to the database under the tripsRef
            Trip trip = new Trip();
            trip.setDateTime(dateTime);
            trip.setDestination(destination.getAddress());
            trip.setPickup(pickup.getAddress());
            trip.setNumPassengers(numPassengers);

            DatabaseReference newTripRef = tripsRef.push();

            newTripRef.setValue(trip)
                    .addOnSuccessListener(aVoid -> {
                        // Retrieve the key (database ID) of the new child
                        String recordKey = newTripRef.getKey();
                        intent.putExtra("recordKey", recordKey);
                        if(!reserve)
                            Toast.makeText(PassengerBookRideActivity.this, "Searching for Drivers...", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(PassengerBookRideActivity.this, "Reserving Ride...", Toast.LENGTH_LONG).show();

                        // Start the PassengerSelectDriverActivity with the Intent
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        String errorMessage = e.getMessage();
                        Log.e("Firebase", "Failed to write data: " + errorMessage);
                        Toast.makeText(PassengerBookRideActivity.this, "No Drivers Available", Toast.LENGTH_LONG).show();
                    });
        } else {

            Toast.makeText(this, "Please Fill All Information", Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeNavBar(){
        NavigationBarView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.home:
                    intent = new Intent(PassengerBookRideActivity.this, SelectRoleActivity.class);
                    startActivity(intent);
                    break;
                case R.id.book:
                    break;
                case R.id.finances:
                    intent = new Intent(PassengerBookRideActivity.this, UsageSummaryActivity.class);
                    intent.putExtra("source", "passenger");
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }
}