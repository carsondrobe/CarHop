package com.example.carpoolapp;

import android.icu.util.Calendar;
import android.location.Address;

public class Trip {
    private Address pickup;
    private Address destination;
    private Calendar dateTime;
    private String name;
    private int numPassengers;

    // Constructors
    public Trip() {
        // Default constructor
    }

    public Trip(Address pickup, Address destination, Calendar dateTime, String name, int numPassengers) {
        this.pickup = pickup;
        this.destination = destination;
        this.dateTime = dateTime;
        this.name = name;
        this.numPassengers = numPassengers;
    }

    // Getter and setter methods
    public Address getPickup() {
        return pickup;
    }

    public void setPickup(Address pickup) {
        this.pickup = pickup;
    }

    public Address getDestination() {
        return destination;
    }

    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
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
