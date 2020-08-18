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
        lvDoneJobsRankList.setAdapter(new ArrayAdapter<User>(getActivity(), R.layout.list_item, users));

        return view;

    }
}
