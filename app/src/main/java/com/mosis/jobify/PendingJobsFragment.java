package com.mosis.jobify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        lvPendingJobs.setAdapter(new ArrayAdapter<Job>(getActivity(), R.layout.layout_pending_job_list_item, R.id.tvJobInfo, jobs));

        return view;

    }

}
