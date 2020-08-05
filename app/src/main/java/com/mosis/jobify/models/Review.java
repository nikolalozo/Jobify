package com.mosis.jobify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Review implements Serializable {
    @Exclude
    public String key;
    public int mark;
    public String comment;
    public String idReviewer;
    public String idReviewed;

    public Review() {
        mark=0;
        comment="Komentar";
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public String getIdReviewed() {
        return idReviewed;
    }

    public void setIdReviewed(String id) {
        this.idReviewed=id;
    }

    public String getIdReviewer() {
        return idReviewer;
    }

    public void setIdReviewer(String id) {
        this.idReviewer = id;
    }
}
