package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

@IgnoreExtraProperties
public class User implements Serializable {
    @Exclude
    public String uID;
    public String email;
    @Exclude
    public String password;
    public String firstName;
    public String lastName;
    public String profession;
    public int jobsPosted;
    public int jobsDone;
    public double lat;
    public double lng;
    public ArrayList<String> connections;

    public User() {
        this.connections=new ArrayList<String>();
    }

    public User(String uID, String email, String password, String firstName, String lastName, String profession) {
        this.uID = uID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession=profession;
        this.connections=new ArrayList<String>();
        jobsPosted=0;
        jobsDone=0;
        lat=0;
        lng=0;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getuID() { return this.uID; }

    public String fullName(){
        return firstName + " " + lastName;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void addConnection(String id) {
        this.connections.add(id);
    }

    public void removeConnection(String id) {
        this.connections.remove(id);
    }

    public ArrayList<String> getConnections() {
        return this.connections;
    }

    @Override
    public String toString() {
        return  firstName + " " + lastName + "\nJobs done: " + jobsDone + "\nJobs posted: " + jobsPosted;
    }


}
