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

import com.mosis.jobify.activities.ProfileActivity;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class DoneJobsRankingListFragment extends Fragment {
    private static final String TAG = "com.mosis.jobify.DoneJobsRankingListFragment";
    private ArrayList<User> users;
    ListView lvDoneJobsRankList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.done_jobs_ranking_list_fragment, container, false);
        users = UsersData.getInstance().users;
        lvDoneJobsRankList = (ListView) view.findViewById((R.id.lvDoneJobsRankList));
        UserAdapter userAdapter = new UserAdapter(getContext(), users);
        userAdapter.setIsRankList(2);
        lvDoneJobsRankList.setAdapter(userAdapter);
        lvDoneJobsRankList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
