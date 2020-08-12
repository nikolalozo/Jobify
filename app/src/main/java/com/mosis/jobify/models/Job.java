package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Job implements Serializable {

    enum Status {
        POSTED,
        REQUESTED,
        DONE
    }

    @Exclude
    public String key;
    public String title;
    public int wage;
    public double longitude;
    public double latitude;
    @Exclude
    public double distance;
    public Status status;
    public String idPosted;
    public String idTaken;

    public Job() {
        title = "New job";
        wage=0;
        longitude=45;
        latitude=23;
        status=Status.POSTED;
    }

    public Job(String title, int wage, double longitude, double latitude, Status status, String idTaken, String idPosted) {
        this.title=title;
        this.wage=wage;
        this.longitude=longitude;
        this.latitude=latitude;
        this.status=status;
        this.idTaken=idTaken;
        this.idPosted=idPosted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage=wage;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude=longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getIdPosted() {
        return idPosted;
    }

    public void setIdPosted(String id) {
        this.idPosted=id;
    }

    public String getIdTaken() {
        return idTaken;
    }

    public void setIdTaken(String id) {
        this.idPosted=id;
    }

    @Override
    public String toString() {
        return "Job{" +
                ", Title=" + title +
                ", Wage=" + wage +
                ", Longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
