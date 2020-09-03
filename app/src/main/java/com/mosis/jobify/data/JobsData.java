package com.mosis.jobify.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosis.jobify.StatusEnum;
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
        ArrayList<Job> jobsForId = new ArrayList<Job>();

        for(int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getIdPosted().equals(id) && jobs.get(i).getStatus() == StatusEnum.POSTED) {
                jobsForId.add(jobs.get(i));
            }
        }
        return jobsForId;
    }

    public float getAverageNoteForUser(String id) {
        float mark = 0;
        int numb = 0;

        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            if (job.getIdPosted().equals(id) && (job.getReviewByEmployeer() > 0)) {
               mark += job.getReviewByEmployeer();
               numb++;
            } else if (job.getIdTaken() != null && job.getIdTaken().equals(id) && (job.getReviewByOwner() > 0)) {
                mark += job.getReviewByOwner();
                numb++;
            }
        }
        if (numb > 0) {
            return mark / numb;
        } else {
            return 0;
        }
    }

    public ArrayList<Job> getPendingJobsForUser(String id) {
        ArrayList<Job> jobsForId = new ArrayList<Job>();

        for(int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).arrayIdRequested.contains(id) && jobs.get(i).getStatus() == StatusEnum.POSTED) {
                jobsForId.add(jobs.get(i));
            }
        }
        return jobsForId;
    }

    public ArrayList<Job> getCurrentJobsForUser(String id) {
        ArrayList<Job> jobsForId = new ArrayList<Job>();

        for (int i = 0; i < jobs.size(); i++) {
            if ((jobs.get(i).arrayIdRequested.contains(id) && jobs.get(i).getStatus() == StatusEnum.TAKEN) || ((jobs.get(i).getIdPosted().equals(id) || (jobs.get(i).getIdTaken() != null && jobs.get(i).getIdTaken().equals(id))) && jobs.get(i).getStatus() == StatusEnum.TAKEN)) {
                jobsForId.add(jobs.get(i));
            }
        }
        return jobsForId;
    }

    public ArrayList<Job> getDoneJobsForCurrentUser() {
        ArrayList<Job> doneJobsForId = new ArrayList<Job>();

        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getIdTaken() != null && jobs.get(i).getIdTaken().equals(UsersData.getInstance().getCurrentUser().getuID()) && jobs.get(i).getStatus() == StatusEnum.DONE) {
                doneJobsForId.add(jobs.get(i));
            }
        }
        return doneJobsForId;
    }

    public ArrayList<Job> getPostedJobsForCurrentUser() {
        ArrayList<Job> postedJobsForId = new ArrayList<Job>();

        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getIdPosted().equals(UsersData.getInstance().getCurrentUser().getuID()) && jobs.get(i).getStatus() == StatusEnum.DONE) {
                postedJobsForId.add(jobs.get(i));
            }
        }
        return postedJobsForId;
    }

    public ArrayList<Job> getPostedJobs() {
        ArrayList<Job> postedJobs = new ArrayList<Job>();

        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).getStatus() == StatusEnum.POSTED) {
                postedJobs.add(jobs.get(i));
            }
        }
        return postedJobs;
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
