package com.mosis.jobify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;

import java.util.ArrayList;

public class JobRequestsFragment extends Fragment {
    private static final String TAG = "com.mosis.jobify.JobRequestsFragment";
    private Button btnTEST;
    private ArrayList<Job> jobs;
    ListView lvJobRequests;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        jobs = JobsData.getInstance().getJobRequestsForUser(UsersData.getInstance().getCurrentUser().getuID());
        View view = inflater.inflate(R.layout.jobrequests_fragment, container, false);
        lvJobRequests = (ListView) view.findViewById((R.id.lvJobRequests));
        lvJobRequests.setAdapter(new JobListAdapter(getActivity(), R.layout.layout_job_list_item,jobs));

        return view;

    }

}
