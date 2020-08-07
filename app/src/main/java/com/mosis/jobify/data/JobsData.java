package com.mosis.jobify.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosis.jobify.models.Job;

import java.util.ArrayList;

public class JobsData {
    ArrayList<Job> jobs;
    private DatabaseReference db;

    public JobsData() {
        this.jobs=new ArrayList<Job>();
        db = FirebaseDatabase.getInstance().getReference();
        addListeners();
    }


    public void addListeners() {
        db.child("jobs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String jobKey = dataSnapshot.getKey();
                Job newJob = dataSnapshot.getValue(Job.class);
                newJob.key=jobKey;
                jobs.add(newJob);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String jobKey = dataSnapshot.getKey();
                Job removedJob = dataSnapshot.getValue(Job.class);
                removedJob.key=jobKey;
                for(int i=0; i<jobs.size(); i++) {
                    if(removedJob.key.equals(jobs.get(i).key)) {
                        jobs.remove(i);
                        i--;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db.child("jobs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void init(){

    }

    public ArrayList<Job> getJobs() {
        return jobs;
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
