package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.CreatedJobDialog;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.Map;

public class NewJobDescriptionActivity extends AppCompatActivity {

    private DatabaseReference db;
    Button btnFinish;
    EditText etJobDescription;
    Job job;
    Map<String, Object> userUpdates;
    public User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_description);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add new job");
        btnFinish = findViewById(R.id.btnFinish);
        etJobDescription = findViewById(R.id.etJobDescription);
        db = FirebaseDatabase.getInstance().getReference("jobs");
        currentUser = UsersData.getInstance().getCurrentUser();
        userUpdates = new HashMap<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            job = (Job) extras.get("job");
        }
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobDescription = etJobDescription.getText().toString();
                job.setDescription(jobDescription);
                String jobId = db.push().getKey();
                db.child(jobId).setValue(job);
                openDialog();
                db = FirebaseDatabase.getInstance().getReference("users");
                DatabaseReference usersRef = db.child(job.getIdPosted());
                currentUser.incrementPostedJobs();
                userUpdates.put("jobsPosted", currentUser.getJobsPosted());
                usersRef.updateChildren(userUpdates);
            }
        });
    }

    public void openDialog() {
        CreatedJobDialog createdJobDialog = new CreatedJobDialog();
        createdJobDialog.show(getSupportFragmentManager(), "created job dialog");
    }
}
