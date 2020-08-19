package com.mosis.jobify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;

import java.util.ArrayList;

public class CurrentJobsFragment extends Fragment {
    private static final String TAG = "com.mosis.jobify.CurrentJobsFragment";
    private ArrayList<Job> jobs;
    ListView lvCurrentJobs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.currentjobs_fragment, container, false);
        openDialog();
        jobs = JobsData.getInstance().getCurrentJobsForUser(UsersData.getInstance().getCurrentUser().getuID());
        lvCurrentJobs = (ListView) view.findViewById((R.id.lvCurrentJobs));
        lvCurrentJobs.setAdapter(new CurrentJobListAdapter(getActivity(), R.layout.layout_current_job_list_item, jobs));

        return view;
    }

    public void openDialog() {
        ConfirmJobDialog confirmJobDialog = new ConfirmJobDialog();
        confirmJobDialog.show(getFragmentManager(), "confirm job dialog");
    }
}