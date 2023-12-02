package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.example.carpoolapp.databinding.ActivityDriverMapBinding;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDriverMapBinding binding;
    private ImageView filter_btn;
    private TextView datePicker;
    private ArrayList<Trip> trips;
    ArrayList markerPoints= new ArrayList();
    private static final String TAG = "YourActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trips = new ArrayList<>();

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

        LatLng kelowna = new LatLng(49.8801, -119.4436);
        float defaultZoom = 12f;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kelowna, defaultZoom));

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        retrieveRecordsFromDatabase();

    }

//    private void getDirections(String from, String to) {
//        try {
//            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + from + "/" + to);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.setPackage("com.google.android.apps.maps");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } catch (ActivityNotFoundException exception) {
//            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=cpm.google.android.apps.maps");
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        }
//    }

    public void showDateTimeDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

                        datePicker.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(DriverMapActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
            }
        };

        new DatePickerDialog(DriverMapActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    private void retrieveRecordsFromDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("trips");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(trips != null) {
                    trips.clear();
                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Trip trip = snapshot.getValue(Trip.class);
                    if (trip != null) {
                        Trip record = new Trip(trip.getPickup(), trip.getDestination(), trip.getDateTime(), trip.getNumPassengers());
                        trips.add(record);

                        LatLng startLatLng = getLocationFromAddress(trip.getPickup());
                        LatLng endLatLng = getLocationFromAddress(trip.getDestination());
                        if (startLatLng != null && endLatLng != null) {
                            // Add a polyline to Google Map for this route
                            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .add(startLatLng, endLatLng));
                            mMap.addMarker(new MarkerOptions().position(startLatLng).title("Pick up"));
                            mMap.addMarker(new MarkerOptions().position(endLatLng).title("Drop off"));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DriverMapActivity.this, "Failed to retrieve records from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private LatLng getLocationFromAddress(String strAddress) {
        Geocoder geocoder = new Geocoder(this); // Use 'this' as the context
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(strAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                Log.d("Geocoding", "Coordinates for " + strAddress + ": " + latLng.toString());

                return latLng;
            } else {
                // Geocoding failed
                Log.d("GeocodingTest", "Geocoding failed for " + strAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("GeocodingTest", "Geocoding exception for " + strAddress + ": " + e.getMessage());
        }
        return null;
    }

}