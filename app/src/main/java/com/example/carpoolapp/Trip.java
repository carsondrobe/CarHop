package com.example.carpoolapp;

import android.icu.util.Calendar;
import android.location.Address;

public class Trip {
    private String pickup;
    private String destination;
    private String dateTime;
    private String name;
    private int numPassengers;

    // Constructors
    public Trip() {
        // Default constructor
    }

    public Trip(String pickup, String destination, String dateTime, String name, int numPassengers) {
        this.pickup = pickup;
        this.destination = destination;
        this.dateTime = dateTime;
        this.name = name;
        this.numPassengers = numPassengers;
    }
    public Trip(String pickup, String destination, String dateTime, int numPassengers) {
        this.pickup = pickup;
        this.destination = destination;
        this.dateTime = dateTime;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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