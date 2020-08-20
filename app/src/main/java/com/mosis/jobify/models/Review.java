package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Review implements Serializable {
    @Exclude
    public String key;
    public float mark;
    public String idJob;

    public Review() {
        mark = 0;
    }

    public Review(float m, String jobId) {
        mark = m;
        idJob = jobId;
    }

    public float getMark() {
        return mark;
    }

    public void setMark(float mark) {
        this.mark = mark;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String id) {
        this.idJob=id;
    }
}
