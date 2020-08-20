package com.mosis.jobify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mosis.jobify.activities.JobActivity;
import com.mosis.jobify.activities.JobHistoryActivity;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.models.Job;

import java.util.ArrayList;

public class DoneJobsHistoryFragment extends Fragment {
    private static final String TAG = "com.mosis.jobify.DoneJobsHistoryFragment";
    private ArrayList<Job> jobs;
    ListView lvDoneJobsHistory;
    TextView emptyDoneJobsHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.done_jobs_history_fragment, container, false);
        jobs = JobsData.getInstance().getDoneJobsForCurrentUser();
        emptyDoneJobsHistory = view.findViewById(R.id.tvEmptyDoneJobsHistory);
        lvDoneJobsHistory = (ListView) view.findViewById((R.id.lvDoneJobsHistory));
        lvDoneJobsHistory.setAdapter(new JobHistoryAdapter(getContext(),jobs));

        lvDoneJobsHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), JobActivity.class);
                i.putExtra("job", jobs.get(position));
                startActivity(i);
            }
        });

        if (jobs.size() > 0) {
            emptyDoneJobsHistory.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
