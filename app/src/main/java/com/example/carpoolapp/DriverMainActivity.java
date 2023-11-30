package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DriverMainActivity extends AppCompatActivity {

    // Create variables
    ImageView backArrow;
    Button bookTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_main);
        // Initialize Buttons
        backArrow = findViewById(R.id.driver_main_ic_BackArrow);
        bookTrip = findViewById(R.id.driver_main_btn_BookANewTrip);

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
//        Intent intent = new Intent(DriverMainActivity.this, DriverActiveTripsActivity.class);
        startActivity(intent);
    }
}