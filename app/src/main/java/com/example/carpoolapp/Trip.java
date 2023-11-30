package com.example.carpoolapp;

import android.icu.util.Calendar;
import android.location.Address;

import java.time.LocalDateTime;

public class Trip {
    private String pickup;
    private String destination;
    private String date; // String to store date
    private String time; // String to store time
    private String name;
    private int numPassengers;

    // Constructors
    public Trip() {
        // Default constructor
    }

    public Trip(String pickup, String destination, String date, String time/*, String name*/, int numPassengers) {
        this.pickup = pickup;
        this.destination = destination;
        this.date = date;
        this.time = time;
//        this.name = name;
        this.numPassengers = numPassengers;
    }

    // Getter and setter methods
    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumPassengers() {
        return numPassengers;
    }

    public void setNumPassengers(int numPassengers) {
        this.numPassengers = numPassengers;
    }
}

