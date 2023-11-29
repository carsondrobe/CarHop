package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.navigation.NavigationBarView;

public class SelectRoleActivity extends AppCompatActivity {
    ImageView passengerButton;
    // TODO: Driver
    ImageView driverButton;

    // NavBar
    NavigationBarView bottomNavigationView;
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

        initializeNavBar();
    }
    private void initializeNavBar(){
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            int selected = bottomNavigationView.getSelectedItemId();
            Log.d("NavBar", "itemID: "+item.getItemId()+" selected: "+selected);
            if (item.getItemId() == selected)
                return false;
            bottomNavigationView.setSelectedItemId(item.getItemId());
            selected = bottomNavigationView.getSelectedItemId();
            Log.d("NavBar", "Now selected: "+selected);
            switch (item.getItemId()){
                case R.id.home:
                    Log.d("NavBar", "to home: ");
                    intent = new Intent(SelectRoleActivity.this, SelectRoleActivity.class);
                    startActivity(intent);
                    break;
                case R.id.book:
                    Log.d("NavBar", "to booking: ");
                    intent = new Intent(SelectRoleActivity.this, PassengerMainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.finances:
                    Log.d("NavBar", "to 'finances': ");
                    intent = new Intent(SelectRoleActivity.this, DriverMainActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(0);

    }
}