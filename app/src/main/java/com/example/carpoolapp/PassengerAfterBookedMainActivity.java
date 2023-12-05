package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PassengerAfterBookedMainActivity extends AppCompatActivity {

    private DatabaseReference ref;
    Button chatPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_after_booked_main);

        // Initialize Firebase Database reference
        ref = FirebaseDatabase.getInstance().getReference();
        chatPopup = findViewById(R.id.afterBooked_btn_chat);


        // Retrieve data from Intent
        Intent intent = getIntent();
        String driverName = intent.getStringExtra("driverName");
        float driverRating = intent.getFloatExtra("driverRating", 0.0f);
        int driverETA = intent.getIntExtra("driverETA", 0);
        String destinationPlaceName = intent.getStringExtra("destination");
        String pickupPlaceName = intent.getStringExtra("pickup");
        String timeBooked = intent.getStringExtra("timeBooked");
        String recordKey = intent.getStringExtra("recordKey");

        // Update TextViews with driver information
        TextView nameTextView = findViewById(R.id.afterBooked_cardview_driverName);
        TextView ratingTextView = findViewById(R.id.afterBooked_cv_driverRating);
        TextView etaTextView = findViewById(R.id.afterBooked_cv_eta);
        TextView topTextView = findViewById(R.id.afterBooked_tv_meetPickup);
        TextView bottomTextView = findViewById(R.id.afterBooked_tv_arrivingBy);

        nameTextView.setText(driverName);
        ratingTextView.setText("Driver Rating: " + driverRating);
        etaTextView.setText("Estimated arrival: " + driverETA + " minutes");
        topTextView.setText("Meet " + driverName + " at " + pickupPlaceName);

        // Calculate the arrival time and display
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date timeBookedDate = sdf.parse(timeBooked);

            Calendar cal = Calendar.getInstance();
            cal.setTime(timeBookedDate);
            cal.add(Calendar.MINUTE, driverETA + 10);

            String arrivalTime = sdf.format(cal.getTime());
            bottomTextView.setText("Arriving at " + destinationPlaceName + " by " + arrivalTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Go to PassengerChatActivity if chatPopup is clicked
        chatPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PassengerAfterBookedMainActivity.this, PassengerChatActivity.class);
                startActivity(i);
            }
        });

        // Set up onClickListener for the "Arrived" button
        MaterialButton arrived = findViewById(R.id.afterBooked_btn_arrived);
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display a toast message
//                Toast.makeText(PassengerAfterBookedMainActivity.this,
//                        "Don't Forget to Rate your Driver!", Toast.LENGTH_SHORT).show();


                // Delete the record from the database
                deleteRecordFromDatabase(recordKey);

                // Start the main activity
                Intent intent = new Intent(PassengerAfterBookedMainActivity.this, PassengerMainActivity.class);
                intent.putExtra("driverName", driverName);
                intent.putExtra("driverRating", driverRating);
                intent.putExtra("destination", destinationPlaceName);
                intent.putExtra("pickup", pickupPlaceName);
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
}
