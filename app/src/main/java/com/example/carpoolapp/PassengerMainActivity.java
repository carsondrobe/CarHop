package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationBarView;

public class PassengerMainActivity extends AppCompatActivity {
    ImageView backArrow;
    Button mainBookRide;
    Button smallBookRide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_main);
        // Initialize Buttons
        backArrow = findViewById(R.id.afterBooked_backArrow);
        mainBookRide = findViewById(R.id.passenger_main_btn_BookRideMain);
        smallBookRide = findViewById(R.id.passenger_main_btn_BookRideSmall);

        // Back to Main Menu onClick back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerMainActivity.this, SelectRoleActivity.class);
                startActivity(intent);
            }
        });
        // Go to PassengerBookRideActivity if Book Ride button clicked
        mainBookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBookRideActivity();
            }
        });
        smallBookRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBookRideActivity();
            }
        });

        initializeNavBar();
    }

    // Launch Book Ride Activity Method for readability
    void startBookRideActivity(){
        Intent intent = new Intent(PassengerMainActivity.this, PassengerBookRideActivity.class);
//        Intent intent = new Intent(PassengerMainActivity.this, PassengerAfterBookedMainActivity.class);
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
                    intent = new Intent(PassengerMainActivity.this, PassengerBookRideActivity.class);
                    startActivity(intent);
                    break;
                case R.id.finances:
                    intent = new Intent(PassengerMainActivity.this, UsageSummaryActivity.class);
                    intent.putExtra("source", "passenger");
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }
}