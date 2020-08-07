package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
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
    public HashMap<String, String> connections;

    public User() {
        this.connections=new HashMap<String, String>();
    }

    public User(String uID, String email, String password, String firstName, String lastName, String profession) {
        this.uID = uID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profession=profession;
        this.connections=new HashMap<String, String>();
        jobsPosted=0;
        jobsDone=0;
        lat=0;
        lng=0;
    }

    public String fullName(){
        return firstName+" "+lastName;
    }

    @Override
    public String toString() {
        return  firstName + " " + lastName + " " + jobsDone + jobsPosted;
    }


}
