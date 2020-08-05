package com.mosis.jobify.models;

public class Location {
    public double lon;
    public double lat;

    public Location() {

    }

    public Location(double lon, double lat) {
        this.lon=lon;
        this.lat=lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon=lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat=lat;
    }

    @Override
    public String toString() {
        return "latitude=" + lat + ", longitude=" + lon;
    }

}
