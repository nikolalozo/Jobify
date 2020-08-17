package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.mosis.jobify.MakeConnectionAdapter;
import com.mosis.jobify.R;
import com.mosis.jobify.activities.MapActivity;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class PeopleInRangeActivity extends AppCompatActivity {
    public ArrayList<User> users;
    public ArrayList<User> nearUsers;
    MapActivity mapActivity;
    ListView lvNearUsers;
    Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User currentUser = UsersData.getInstance().getCurrentUser();
        nearUsers = new ArrayList<User>();
        setContentView(R.layout.activity_people_in_range);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("People in range");
        lvNearUsers = findViewById((R.id.lvNearUsers));
        users = UsersData.getInstance().users;
        for (int i = 0; i < users.size(); i++) {
            double distance = mapActivity.pointsDistance(currentUser.getLat(), currentUser.getLng(), users.get(i).getLat(), users.get(i).getLng());
            if (distance <= 100 && !users.get(i).getuID().equals(currentUser.getuID())) {
                nearUsers.add(users.get(i));
            }
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            job = (Job) extras.get("job");
            lvNearUsers.setAdapter(new MakeConnectionAdapter(PeopleInRangeActivity.this, R.layout.layout_make_connection_list_item, nearUsers, job));
        }


    }
}
