package com.mosis.jobify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mosis.jobify.activities.JobActivity;
import com.mosis.jobify.activities.ProfileActivity;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class PostedJobsRankingListFragment extends Fragment {
    private static final String TAG = "com.mosis.jobify.PostedJobsRankingListFragment";
    private ArrayList<User> users;
    ListView lvPostedJobsRankList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posted_jobs_ranking_list_fragment, container, false);
        users = UsersData.getInstance().usersPosted;
        lvPostedJobsRankList = (ListView) view.findViewById((R.id.lvPostedJobsRankList));
        UserAdapter userAdapter = new UserAdapter(getContext(), users);
        userAdapter.setIsRankList(1);
        lvPostedJobsRankList.setAdapter(userAdapter);
        lvPostedJobsRankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", (User) users.get(position));
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        return view;
    }
}