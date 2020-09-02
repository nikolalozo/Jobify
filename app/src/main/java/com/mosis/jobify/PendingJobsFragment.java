package com.mosis.jobify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mosis.jobify.activities.JobActivity;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;

import java.util.ArrayList;

public class PendingJobsFragment extends Fragment {
    private static final String TAG = "com.mosis.jobify.PendingJobsFragment";
    private ArrayList<Job> jobs;
    ListView lvPendingJobs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pendingjobs_fragment, container, false);
        jobs = JobsData.getInstance().getPendingJobsForUser(UsersData.getInstance().getCurrentUser().getuID());
        lvPendingJobs = (ListView) view.findViewById((R.id.lvPendingJobs));
        lvPendingJobs.setAdapter(new PendingJobsAdapter(view.getContext(),jobs));
        lvPendingJobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("job", (Job) jobs.get(position));
                Intent i = new Intent(getActivity(), JobActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        return view;

    }

}
