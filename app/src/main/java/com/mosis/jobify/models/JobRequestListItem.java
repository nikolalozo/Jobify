package com.mosis.jobify.models;

import java.util.Date;

public class JobRequestListItem {
    public User user;
    public String jobId;
    public String jobTitle;
    public Date date;

    public JobRequestListItem(User user, String jobId, String jobTitle, Date date) {
        this.user = user;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.date = date;
    }
}
