package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PassengerSelectDriverActivity extends AppCompatActivity {
    DatabaseReference ref;
    DriverInfo selectedDriver;
    MaterialButton confirm;
    TextView price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_select_driver);
        final String apikey = BuildConfig.apikey;
        Places.initialize(getApplicationContext(), apikey);
        ref = FirebaseDatabase.getInstance().getReference();
        confirm = findViewById(R.id.activity_passenger_book_ride_btn_select_driver4);
        price = findViewById(R.id.priceShow);

        Intent intent = getIntent();
        int numPassengers = intent.getIntExtra("numPassengers", 0);
        boolean reserve = intent.getBooleanExtra("reserve", false);
        String pickupPlaceName = intent.getStringExtra("pickupPlaceName");
        String pickupPlaceId = intent.getStringExtra("pickupPlaceId");
        String destinationPlaceName = intent.getStringExtra("destinationPlaceName");
        String destinationPlaceId = intent.getStringExtra("destinationPlaceId");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String recordKey = intent.getStringExtra("recordKey");


        // Log values
        Log.d("PassengerSelectDriver", "numPassengers: " + numPassengers);
        Log.d("PassengerSelectDriver", "reserve: " + reserve);
        Log.d("PassengerSelectDriver", "pickupPlaceName: " + pickupPlaceName);
        Log.d("PassengerSelectDriver", "pickupPlaceId: " + pickupPlaceId);
        Log.d("PassengerSelectDriver", "destinationPlaceName: " + destinationPlaceName);
        Log.d("PassengerSelectDriver", "destinationPlaceId: " + destinationPlaceId);
        Log.d("PassengerSelectDriver", "date: " + date);
        Log.d("PassengerSelectDriver", "time: " + time);

        MaterialButton btn1 = findViewById(R.id.afterBooked_btn_chat);
        MaterialButton btn2 = findViewById(R.id.passenger_select_driver_btn_select2);

        DriverInfo driverInfo1 = new DriverInfo("John Doe",
                4.5f,
                5);
        DriverInfo driverInfo2 = new DriverInfo("Davis Franklin",
                4.1f,
                8);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                btn1.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                btn1.setText("SELECTED");
                selectedDriver = driverInfo1;
                price.setText("$5");

                btn2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                btn2.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                btn2.setText("Select");
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                btn2.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                btn2.setText("SELECTED");
                selectedDriver = driverInfo2;
                price.setText("$5");

                btn1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                btn1.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                btn1.setText("Select");
            }
        });

        ImageView backarrow = findViewById(R.id.passenger_select_driver_ic_backArrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecordFromDatabase(recordKey);
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the next activity
                Toast.makeText(PassengerSelectDriverActivity.this, "Connecting you with Driver...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PassengerSelectDriverActivity.this, PassengerAfterBookedMainActivity.class);

                // Pass the selected driver information as extras in the Intent
                intent.putExtra("driverName", selectedDriver.getName());
                intent.putExtra("driverRating", selectedDriver.getRating());
                intent.putExtra("driverETA", selectedDriver.getEta());
                intent.putExtra("destination", destinationPlaceName);
                intent.putExtra("pickup", pickupPlaceName);
                intent.putExtra("timeBooked", time);
                intent.putExtra("recordKey", recordKey);

                // Start the next activity
                startActivity(intent);
            }
        });


    }

    private void deleteRecordFromDatabase(String recordKey){
        // Get the reference to the specific record using the key
        DatabaseReference recordRef = ref.child("trips").child(recordKey);

        // Remove the record from the database
        recordRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Deletion successful
                    Log.d("PassengerSelectDriver", "Record deleted successfully");
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    String errorMessage = e.getMessage();
                    Log.e("PassengerSelectDriver", "Failed to delete record: " + errorMessage);
                });
    }
}