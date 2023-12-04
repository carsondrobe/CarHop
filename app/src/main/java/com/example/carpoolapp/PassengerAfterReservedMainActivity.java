package com.example.carpoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PassengerAfterReservedMainActivity extends AppCompatActivity {
    ImageView backArrow;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_after_reserved_main);
        // Initialize Buttons
        backArrow = findViewById(R.id.afterBooked_backArrow);
        ref = FirebaseDatabase.getInstance().getReference();

        // Back to Main Menu onClick back arrow
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PassengerAfterReservedMainActivity.this, SelectRoleActivity.class);
                startActivity(intent);
            }
        });
        initializeNavBar();

        // Get the intent that started this activity
        Intent intent = getIntent();

        // Retrieve the values from the intent
        int numPassengers = intent.getIntExtra("numPassengers", 0);
        boolean reserve = intent.getBooleanExtra("reserve", false);
        String pickupPlaceName = intent.getStringExtra("pickupPlaceName");
        String pickupPlaceId = intent.getStringExtra("pickupPlaceId");
        String destinationPlaceName = intent.getStringExtra("destinationPlaceName");
        String destinationPlaceId = intent.getStringExtra("destinationPlaceId");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String recordKey = intent.getStringExtra("recordKey");

        TextView dateTextView = findViewById(R.id.afterReserved_Date);
        TextView pickupTextView = findViewById(R.id.afterReserved_Date_From);
        TextView dropoffTextView = findViewById(R.id.afterReserved_dropoff);
        TextView passengersTextView = findViewById(R.id.afterReserved_numPassengers);

        dateTextView.setText(date + " at " + time);
        pickupTextView.setText("From: " + pickupPlaceName);
        dropoffTextView.setText("To: " + destinationPlaceName);
        passengersTextView.setText("Passengers: " + numPassengers);

        MaterialButton cancel = findViewById(R.id.afterReserved_btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecordFromDatabase(recordKey);
                Toast.makeText(PassengerAfterReservedMainActivity.this,
                        "Cancelling Ride...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PassengerAfterReservedMainActivity.this, PassengerMainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
    private void deleteRecordFromDatabase(String recordKey){
        DatabaseReference recordRef = ref.child("trips").child(recordKey);

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
    private void initializeNavBar(){
        NavigationBarView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.home:
                    break;
                case R.id.book:
                    intent = new Intent(PassengerAfterReservedMainActivity.this, PassengerBookRideActivity.class);
                    startActivity(intent);
                    break;
                case R.id.finances:
                    intent = new Intent(PassengerAfterReservedMainActivity.this, UsageSummaryActivity.class);
                    intent.putExtra("source", "passenger");
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }
}