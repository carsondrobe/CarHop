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