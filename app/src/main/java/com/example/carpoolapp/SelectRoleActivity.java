package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectRoleActivity extends AppCompatActivity {
    ImageView passengerButton;
    // TODO: Driver
    ImageView driverButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_role);
        // Initialize Buttons
        passengerButton = findViewById(R.id.activity_main_img_person);
        driverButton = findViewById(R.id.activity_main_img_driver);

        // On Passenger Button Click, Launch PassengerMainActivity
        passengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectRoleActivity.this, PassengerMainActivity.class);
                startActivity(intent);
            }
        });
        // On driver button click, launch DriverMainActivity
        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelectRoleActivity.this, DriverMainActivity.class);
                startActivity(i);
            }
        });
    }
}