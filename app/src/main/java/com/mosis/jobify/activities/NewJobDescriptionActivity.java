package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mosis.jobify.R;
import com.mosis.jobify.models.Job;

public class NewJobDescriptionActivity extends AppCompatActivity {
    private static final String TAG = "NewJobDescription";

    Button btnFinish;
    EditText etJobDescription;
    Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_description);
        btnFinish = findViewById(R.id.btnFinish);
        etJobDescription = findViewById(R.id.etJobDescription);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            job = (Job) extras.get("job");
            Log.i(TAG, "Job " + job);
        }
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobDescription = etJobDescription.getText().toString();
                job.setDescription(jobDescription);


            }
        });
    }
}
