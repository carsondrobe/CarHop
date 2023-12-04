package com.example.carpoolapp;

public class DriverInfo {
    private String name;
    private float rating;
    private int eta;
    public DriverInfo(String name, float rating, int eta) {
        this.name = name;
        this.rating = rating;
        this.eta = eta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }
}
