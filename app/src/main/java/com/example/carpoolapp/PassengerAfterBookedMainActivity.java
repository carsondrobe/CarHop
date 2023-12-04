package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_after_booked_main);
        ref = FirebaseDatabase.getInstance().getReference();

        // Retrieve the data from the Intent

        Intent intent = getIntent();
        String driverName = intent.getStringExtra("driverName");
        float driverRating = intent.getFloatExtra("driverRating", 0.0f);
        int driverETA = intent.getIntExtra("driverETA", 0);
        String destinationPlaceName = intent.getStringExtra("destination");
        String pickupPlaceName = intent.getStringExtra("pickup");
        String timeBooked = intent.getStringExtra("timeBooked");
        String recordKey = intent.getStringExtra("recordKey");

        // Now you have the data, you can use it as needed in your activity
        // For example, update TextViews with the driver information
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
            // Parse the timeBooked string to a Date object
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date timeBookedDate = sdf.parse(timeBooked);

            // Add driverETA and 10 minutes to the time
            Calendar cal = Calendar.getInstance();
            cal.setTime(timeBookedDate);
            cal.add(Calendar.MINUTE, driverETA + 10);

            // Format the result back to a string
            String arrivalTime = sdf.format(cal.getTime());

            // Set the text for bottomTextView
            bottomTextView.setText("Arriving at " + destinationPlaceName + " by " + arrivalTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        MaterialButton arrived = findViewById(R.id.afterBooked_btn_arrived);
        arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PassengerAfterBookedMainActivity.this, "Don't Forget to Rate your Driver!", Toast.LENGTH_SHORT).show();

                // Delete Record
                deleteRecordFromDatabase(recordKey);
                Intent intent = new Intent(PassengerAfterBookedMainActivity.this, PassengerMainActivity.class);

                // Pass ride information to the main activity
                intent.putExtra("driverName", driverName);
                intent.putExtra("driverRating", driverRating);
                intent.putExtra("destination", destinationPlaceName);
                intent.putExtra("pickup", pickupPlaceName);

                // Start the main activity
                startActivity(intent);
                finish();
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