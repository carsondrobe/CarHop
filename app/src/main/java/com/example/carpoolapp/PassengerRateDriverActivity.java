package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PassengerRateDriverActivity extends AppCompatActivity {

    // Create variables
    RadioGroup rg;
    Button submit;
    Button skip;

    String driverName;
    float driverRating;
    String destinationPlaceName;
    String pickupPlaceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_rate_driver);
        // Initialize variables
        rg = findViewById(R.id.rateDriver_radioGroup);
        submit = findViewById(R.id.rateDriver_submit);
        skip = findViewById(R.id.rateDriver_skip);
        // Retrieve data from Intent
        Intent intent = getIntent();
        driverName = intent.getStringExtra("driverName");
        driverRating = intent.getFloatExtra("driverRating", 0.0f);
        destinationPlaceName = intent.getStringExtra("destination");
        pickupPlaceName = intent.getStringExtra("pickup");
        // If passenger clicks skip, take them to PassengerMainActivity
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerRateDriverActivity.this, PassengerMainActivity.class);
                intent.putExtra("driverName", driverName);
                intent.putExtra("driverRating", driverRating);
                intent.putExtra("destination", destinationPlaceName);
                intent.putExtra("pickup", pickupPlaceName);
                startActivity(intent);
                finish();
            }
        });
        // If passenger clicks submit, then get and store rating and take them  to PassengerMainActivity
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get selected rating
                int selectedId = rg.getCheckedRadioButtonId();
                switch(selectedId) {
                    case R.id.rateDriver_rb_1star:
                        driverRating = 1;
                    case R.id.rateDriver_rb_2star:
                        driverRating = 2;
                    case R.id.rateDriver_rb_3star:
                        driverRating = 3;
                    case R.id.rateDriver_rb_4star:
                        driverRating = 4;
                    default:
                        driverRating = 5;
                }
                if(selectedId == -1) {
                    // Display a toast message saying they have to select a rating before submitting a review
                    Toast.makeText(PassengerRateDriverActivity.this,
                            "Please select a rating to submit a review.", Toast.LENGTH_SHORT).show();
                } else {
                    // Display a toast message saying their review was submitted and thank you
                    Toast.makeText(PassengerRateDriverActivity.this,
                            "Thank you for your review!.", Toast.LENGTH_SHORT).show();
                    // Take them to the next page
                    Intent intent = new Intent(PassengerRateDriverActivity.this, PassengerMainActivity.class);
                    intent.putExtra("driverName", driverName);
                    intent.putExtra("driverRating", driverRating);
                    intent.putExtra("destination", destinationPlaceName);
                    intent.putExtra("pickup", pickupPlaceName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}