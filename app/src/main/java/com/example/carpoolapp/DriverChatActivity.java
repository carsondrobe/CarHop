package com.example.carpoolapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DriverChatActivity extends AppCompatActivity {

    // Create variables
    EditText messageBox;
    LinearLayout msgContainer;
    Button send;
    String[] responses = {"Hello!", "Yeah, just myself.", "Great, see you soon!"};
    int counter = 0;
    TextView passengerTyping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_chat);
        // Initialize variables
        messageBox = findViewById(R.id.driver_chat_et_messageBox);
        send = findViewById(R.id.driver_chat_btn_send);
        msgContainer = findViewById(R.id.driver_chat_messageContainer);
        // When send button is clicked, generate response
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display driver message
                displayMessage("You: " + messageBox.getText().toString().trim() + "\n", true);
                // Display that the passenger is typing after 1 second
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        passengerTyping = displayMessage("PassengerName is typing..." + "\n", false);
                    }
                }, 1000);
                // Display the passenger response after 3 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Delete passenger typing textview
                        msgContainer.removeView(passengerTyping);
                        // Display passenger response
                        displayMessage("PassengerName: " + responses[counter] + "\n", false);
                        // Increment counter
                        counter++;
                    }
                }, 4000);
                // Clear message box field
                messageBox.setText("");
            }
        });
    }

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
        NestedScrollView scrollView = findViewById(R.id.driver_chat_scrollView);
        scrollView.fullScroll(View.FOCUS_DOWN);
        // Return textview
        return tv;
    }
}