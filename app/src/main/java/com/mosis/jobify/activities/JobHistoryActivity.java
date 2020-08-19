package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mosis.jobify.JobHistoryAdapter;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.models.Job;

import java.util.ArrayList;

public class JobHistoryActivity extends AppCompatActivity {
    public ArrayList<Job> doneJobs;
    ListView lvDoneJobs;
    TextView tvEmptyJobHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history);

        doneJobs = JobsData.getInstance().getDoneJobsForCurrentUser();
        lvDoneJobs = findViewById((R.id.lvDoneJobs));
        tvEmptyJobHistory = findViewById(R.id.tvEmptyJobHistory);
        lvDoneJobs.setAdapter(new JobHistoryAdapter(this, doneJobs));
        if (doneJobs.size() > 0) {
            tvEmptyJobHistory.setVisibility(View.INVISIBLE);
        }

        lvDoneJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(JobHistoryActivity.this, JobActivity.class);
                i.putExtra("job", doneJobs.get(position));
                startActivity(i);
            }
        });
    }
}
