package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

public class PassengerChatActivity extends AppCompatActivity {
    // Create variables
    EditText messageBox;
    LinearLayout msgContainer;
    Button send;
    String[] responses = {"Hi!", "Yeah, that's what is on my GPS!.", "I'll be there in 15 minutes!", "See you soon!"};
    int counter = 0;
    TextView driverTyping;
    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_chat);
        // Initialize variables
        messageBox = findViewById(R.id.passenger_chat_et_messageBox);
        send = findViewById(R.id.passenger_chat_btn_send);
        msgContainer = findViewById(R.id.passenger_chat_messageContainer);
        backArrow = findViewById(R.id.passenger_chat_ic_backArrow);
        // Go to PassengerAfterReservedMainActivity if back arrow is clicked
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PassengerChatActivity.this, PassengerAfterReservedMainActivity.class);
                startActivity(i);
            }
        });
        // When send button is clicked, generate response
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("YourTag", "Clicked send button");
                // Display passenger message
                displayMessage("You: " + messageBox.getText().toString().trim() + "\n", true);
                // Display that the driver is typing after 1 second
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        driverTyping = displayMessage("Driver is typing..." + "\n", false);
                    }
                }, 1000);
                // Display the driver response after 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Delete passenger typing textview
                        msgContainer.removeView(driverTyping);
                        // Display passenger response
                        displayMessage("Driver: " + responses[counter] + "\n", false);
                        // Increment counter if possible
                        if(counter+1 < responses.length) {
                            counter++;
                        } else {
                            // Set counter to 0
                            counter = 0;
                        }
                    }
                }, 4000);
                // Clear message box field
                messageBox.setText("");
            }
        });
    }

    // Function to create a textview that is added to the message container
    TextView displayMessage(String message, boolean user) {
        // Create new textview with message
        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setTextSize(18);
        // If message is user message, display on left side
        if (user) {
            tv.setGravity(Gravity.START);
        } else {
            // Else assume message is passenger response and display on right side
            tv.setGravity(Gravity.END);
        }
        // Display new chat
        msgContainer.addView(tv);
        // Scroll to the bottom of the chatTextView
        NestedScrollView scrollView = findViewById(R.id.passenger_chat_scrollView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        // Return textview
        return tv;
    }
}