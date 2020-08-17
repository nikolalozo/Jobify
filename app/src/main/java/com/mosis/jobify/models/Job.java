package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.mosis.jobify.StatusEnum;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

@IgnoreExtraProperties
public class Job implements Serializable {
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
    public StatusEnum status;
    public String idTaken;
    public String idPosted;
    public ArrayList<String> arrayIdRequested;

    public Job() {
        title = "New job";
        description = "";
        wage=0;
        longitude=45;
        latitude=23;
        status=StatusEnum.POSTED;
        date = new Date();
        datePosted = new Date();
        appliedBy = new Date();
        arrayIdRequested = new ArrayList<String>();
    }

    public Job(String title, int wage, String desc, double longitude, double latitude, StatusEnum status, String idTaken, Date date, Date datePosted, Date appliedBy) {
        this.title=title;
        this.wage=wage;
        this.description = desc;
        this.longitude=longitude;
        this.latitude=latitude;
        this.status=status;
        this.idTaken=idTaken;
        this.date = date;
        this.datePosted = datePosted;
        this.appliedBy = appliedBy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getIdTaken() {
        return idTaken;
    }

    public void setIdTaken(String id) {
        this.idTaken=id;
    }

    public String getIdPosted() {
        return idPosted;
    }

    public void setIdPosted(String id) {
        this.idPosted = id;
    }

    public StatusEnum getStatus() { return this.status; }

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

    public void addRequest(String id) {
        this.arrayIdRequested.add(id);
    }

    public void removeRequest(String id) {
        this.arrayIdRequested.remove(id);
    }

    @Override
    public String toString() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        return title + "\n" + dateFormat.format(date);
    }
}
