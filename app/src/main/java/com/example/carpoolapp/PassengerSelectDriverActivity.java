package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;

public class PassengerSelectDriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_select_driver);
        final String apikey = BuildConfig.apikey;
        Places.initialize(getApplicationContext(), apikey);

        Intent intent = getIntent();
        int numPassengers = intent.getIntExtra("numPassengers", 0);
        boolean reserve = intent.getBooleanExtra("reserve", false);
        String pickupPlaceName = intent.getStringExtra("pickupPlaceName");
        String pickupPlaceId = intent.getStringExtra("pickupPlaceId");
        String destinationPlaceName = intent.getStringExtra("destinationPlaceName");
        String destinationPlaceId = intent.getStringExtra("destinationPlaceId");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");

        // Log values
        Log.d("PassengerSelectDriver", "numPassengers: " + numPassengers);
        Log.d("PassengerSelectDriver", "reserve: " + reserve);
        Log.d("PassengerSelectDriver", "pickupPlaceName: " + pickupPlaceName);
        Log.d("PassengerSelectDriver", "pickupPlaceId: " + pickupPlaceId);
        Log.d("PassengerSelectDriver", "destinationPlaceName: " + destinationPlaceName);
        Log.d("PassengerSelectDriver", "destinationPlaceId: " + destinationPlaceId);
        Log.d("PassengerSelectDriver", "date: " + date);
        Log.d("PassengerSelectDriver", "time: " + time);

    }
}