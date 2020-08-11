package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class Job implements Serializable {

    enum Status {
        PUBLISHED,
        REQUESTED,
        DONE
    }

    @Exclude
    public String key;
    public String title;
    public int wage;
    public double longitude;
    public double latitude;
    public Date date;
    public Date datePosted;
    public Date appliedBy;
    public String description;
    @Exclude
    public double distance;
    public Status status;
    public String idPosted;
    public String idTaken;

    public Job() {
        title = "New job";
        description = "";
        wage=0;
        longitude=45;
        latitude=23;
        status=Status.PUBLISHED;
        date = new Date();
        datePosted = new Date();
        appliedBy = new Date();
    }

    public Job(String title, int wage, String desc, double longitude, double latitude, Status status, String idTaken, String idPosted, Date date, Date datePosted, Date appliedBy) {
        this.title=title;
        this.wage=wage;
        this.description = desc;
        this.longitude=longitude;
        this.latitude=latitude;
        this.status=status;
        this.idTaken=idTaken;
        this.idPosted=idPosted;
        this.date = date;
        this.datePosted = datePosted;
        this.appliedBy = appliedBy;
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

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDatePosted() {
        return this.datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Date getAppliedBy() {
        return this.appliedBy;
    }

    public void setAppliedBy(Date appliedBy) {
        this.appliedBy = appliedBy;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public String toString() {
        return "Job{" +
                ", Title=" + title +
                ", Wage=" + wage +
                ", Description=" + description +
                ", Longitude=" + longitude +
                ", latitude=" + latitude +
                ", Date=" + date +
                ", Date Posted=" + datePosted +
                ", Applied By=" + appliedBy +
                '}';
    }
}
