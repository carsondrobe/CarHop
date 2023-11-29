package com.example.carpoolapp;

import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.example.carpoolapp.databinding.ActivityDriverMapBinding;

public class DriverMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDriverMapBinding binding;
    private Button filter_btn;
    private TextView datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datePicker = findViewById(R.id.datePicker_text);
        filter_btn = findViewById(R.id.filter_btn);

        binding = ActivityDriverMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng kelowna = new LatLng(49.8801, -119.4436);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kelowna));

        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void showTrack(String from, String to){
        try{
            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + from + "/" + to);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=cpm.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        }
    }

    public void showDatePickerDialog(View v) {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Set the selected date to the TextView
                String selectedDate = day + "/" + (month + 1) + "/" + year;
                datePicker.setText("Selected Date: " + selectedDate);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }
}