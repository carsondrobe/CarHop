package com.example.carpoolapp;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

public class SelectRoleActivity extends AppCompatActivity {
    Button passengerButton;
    // TODO: Driver
    Button driverButton;

    // NavBar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        // Initialize Buttons
        passengerButton = findViewById(R.id.activity_select_role_btn_passengerMode);
        driverButton = findViewById(R.id.activity_select_role_btn_driverMode);

        // On Passenger Button Click, Launch PassengerMainActivity
        passengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRoleActivity.this, PassengerMainActivity.class);
                startActivity(intent);
//                // Create an Intent to start the next activity
//                Toast.makeText(SelectRoleActivity.this, "Connecting you with Driver...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SelectRoleActivity.this, PassengerAfterBookedMainActivity.class);
//                String driverName = "John Doe";
//                float driverRating = 0;
//                int driverETA = 15;
//                String destinationPlaceName = "There";
//                String pickupPlaceName = "Here";
//                String timeBooked = "12:00";
//                String recordKey = "a1";
//                // Pass the selected driver information as extras in the Intent
//                intent.putExtra("driverName", driverName);
//                intent.putExtra("driverRating", driverRating);
//                intent.putExtra("driverETA", driverETA);
//                intent.putExtra("destination", destinationPlaceName);
//                intent.putExtra("pickup", pickupPlaceName);
//                intent.putExtra("timeBooked", timeBooked);
//                intent.putExtra("recordKey", recordKey);
//                // Start the next activity
//                startActivity(intent);
            }
        });
        // On driver button click, launch DriverMainActivity
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRoleActivity.this, DriverMainActivity.class);
//                Intent intent = new Intent(SelectRoleActivity.this, DriverActiveTripsActivity.class);
                startActivity(intent);
            }
        });
    }
}