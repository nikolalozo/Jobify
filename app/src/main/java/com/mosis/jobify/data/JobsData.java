package com.mosis.jobify.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class JobsData {
    ArrayList<Job> jobs;
    private DatabaseReference db;

    public JobsData() {
        this.jobs = new ArrayList<Job>();
        db = FirebaseDatabase.getInstance().getReference().child("jobs");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jobs.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    Job job = snap.getValue(Job.class);
                    job.key=snap.getKey();
                    jobs.add(0, job);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void init(){

    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public ArrayList<Job> getJobRequestsForUser(String id) {
        final ArrayList<Job> jobsForId = new ArrayList<Job>();
        Query queryRef = db.orderByChild("idPosted").equalTo(id);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                jobsForId.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    Job job = snap.getValue(Job.class);
                    if (!job.arrayIdRequested.isEmpty()) {
                        jobsForId.add(0, job);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return jobsForId;
    }

    public Job getJob(int i) {
        return this.jobs.get(i);
    }

    private static class SingletonHolder {
        private static final JobsData instance = new JobsData();
    }

    public static JobsData getInstance() {
        return SingletonHolder.instance;
    }

}
