package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.ConfirmJobDialog;
import com.mosis.jobify.CreatedJobDialog;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.Map;

public class NewJobDescriptionActivity extends AppCompatActivity {
    Toolbar toolbar;
    private DatabaseReference db;
    Button btnFinish, btnCancel;
    EditText etJobDescription;
    Job job;
    Map<String, Object> userUpdates;
    public User currentUser;
    String jobDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_description);
        btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setEnabled(false);
        btnCancel = findViewById(R.id.btnCancel);
        etJobDescription = findViewById(R.id.etJobDescription);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Add new job");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        db = FirebaseDatabase.getInstance().getReference("jobs");
        currentUser = UsersData.getInstance().getCurrentUser();
        userUpdates = new HashMap<>();
        etJobDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)  {
                if (charSequence.length() > 0) {
                    btnFinish.setEnabled(true);
                    jobDescription = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    btnFinish.setEnabled(true);
                    jobDescription = editable.toString();
                } else {
                    btnFinish.setEnabled(false);
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            job = (Job) extras.get("job");
        }
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });
    }

    public void openDialog() {
        CreatedJobDialog createdJobDialog = new CreatedJobDialog();
        createdJobDialog.show(getSupportFragmentManager(), "created job dialog");
    }

    public void cancelDialog() {
        ConfirmJobDialog confirmJobDialog = new ConfirmJobDialog();
        confirmJobDialog.cancelJob();
        confirmJobDialog.setMessage("Are you sure you want to cancel the job?");
        confirmJobDialog.show(getSupportFragmentManager(), "confirm job dialog");
    }
}
