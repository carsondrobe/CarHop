package com.example.carpoolapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.net.ParseException;
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
import com.google.android.material.navigation.NavigationBarView;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDriverMapBinding binding;
    private ImageView filter_btn;
    private TextView datePicker;
    private ArrayList<Trip> trips;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private static final String TAG = "YourActivity";
    private boolean filter;
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trips = new ArrayList<>();

        datePicker = findViewById(R.id.datePicker_text);
        filter_btn = findViewById(R.id.filter_btn);
        backArrow = findViewById(R.id.driver_main_ic_BackArrow);
        initializeNavBar();

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

                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        selectedHour = hourOfDay;
                        selectedMinute = minute;

                        filter = true;
                        mMap.clear();
                        retrieveRecordsFromDatabase();
                    }
                };

                new TimePickerDialog(DriverMapActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };

        new DatePickerDialog(DriverMapActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        filter = true;
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                        try {
                            Date parsedDate = dateFormat.parse(trip.getDateTime());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(parsedDate);

                            int year = calendar.get(Calendar.YEAR);
                            int month = calendar.get(Calendar.MONTH);
                            int day = calendar.get(Calendar.DAY_OF_MONTH);
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int minute = calendar.get(Calendar.MINUTE);

                            if (startLatLng != null && endLatLng != null) {
                                if (filter) {
                                    Log.d("Filter", "Selected Year: " + selectedYear + " | Year: " + year);
                                    Log.d("Filter", "Selected Month: " + selectedMonth + " | Month: " + month);
                                    Log.d("Filter", "Selected Day: " + selectedDay + " | Day: " + day);
                                    Log.d("Filter", "Selected Hour: " + selectedHour + " | Hour: " + hour);
                                    Log.d("Filter", "Selected Minute: " + selectedMinute + " | Minute: " + minute);

                                    if (selectedYear == year && selectedMonth == month && selectedDay == day &&
                                            selectedHour == hour && (selectedMinute <= minute + 15 && selectedMinute >= minute - 15)) {
                                        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                                .clickable(true)
                                                .add(startLatLng, endLatLng));

                                        // Associate the polyline with the trip details and add the click listener
                                        polyline.setTag(record); // record is the current Trip object
                                        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                                            @Override
                                            public void onPolylineClick(Polyline polyline) {
                                                // Retrieve the associated Trip object when a polyline is clicked
                                                Trip clickedTrip = (Trip) polyline.getTag();
                                                if (clickedTrip != null) {
                                                    showPopupDialog(clickedTrip.getPickup(), clickedTrip.getDestination(),
                                                            clickedTrip.getNumPassengers(), clickedTrip.getDateTime());
                                                }
                                            }
                                        });

                                        mMap.addMarker(new MarkerOptions().position(startLatLng).title("Pick up"));
                                        mMap.addMarker(new MarkerOptions().position(endLatLng).title("Drop off"));
                                    }
                                } else {
                                    Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                            .clickable(true)
                                            .add(startLatLng, endLatLng));

                                    // Associate the polyline with the trip details and add the click listener
                                    polyline.setTag(record); // record is the current Trip object
                                    mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                                        @Override
                                        public void onPolylineClick(Polyline polyline) {
                                            // Retrieve the associated Trip object when a polyline is clicked
                                            Trip clickedTrip = (Trip) polyline.getTag();
                                            if (clickedTrip != null) {
                                                showPopupDialog(clickedTrip.getPickup(), clickedTrip.getDestination(),
                                                        clickedTrip.getNumPassengers(), clickedTrip.getDateTime());
                                            }
                                        }
                                    });

                                    mMap.addMarker(new MarkerOptions().position(startLatLng).title("Pick up"));
                                }
                            }
                        } catch (ParseException | java.text.ParseException e) {
                            e.printStackTrace();
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
    private void showPopupDialog(String pickup, String destination, int numPassengers, String dateTime) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.driver_map_popup);

        TextView pickupTextView = dialog.findViewById(R.id.pickupTextView);
        TextView destinationTextView = dialog.findViewById(R.id.destinationTextView);
        TextView passengersTextView = dialog.findViewById(R.id.passengersTextView);
        TextView dateTextView = dialog.findViewById(R.id.dateTextView);
        Button bookRideButton = dialog.findViewById(R.id.bookRideButton);

        pickupTextView.setText("From: \n" + pickup);
        destinationTextView.setText("To: \n" + destination);
        passengersTextView.setText("Number of Passengers: " + numPassengers);
        dateTextView.setText("Date & Time: " + dateTime);

        bookRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverMapActivity.this, DriverTripsSelectedActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt("numPassengers", numPassengers);
                bundle.putString("pickup", pickup);
                bundle.putString("destination", destination);
                bundle.putString("dateTime", dateTime);

                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Booking ride...", Toast.LENGTH_SHORT).show();
                dialog.dismiss(); // Dismiss the dialog after action is performed
            }
        });

        dialog.show();
    }
    public void backClick(View v) {
        Intent intent = new Intent(DriverMapActivity.this, DriverMainActivity.class);
        startActivity(intent);
    }
    private void initializeNavBar(){
        NavigationBarView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.home:
                    intent = new Intent(DriverMapActivity.this, DriverMainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.book:
                    break;
                case R.id.finances:
                    intent = new Intent(DriverMapActivity.this, UsageSummaryActivity.class);
                    intent.putExtra("source", "driver");
                    startActivity(intent);
                    break;
            }
            return true;
        });
    }
}