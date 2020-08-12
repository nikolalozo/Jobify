package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mosis.jobify.JobsActivity;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

public class RankingActivity extends AppCompatActivity {
    ListView lvDone, lvPosted;
    public ArrayAdapter<User> adapterDone, adapterPosted;
    TextView tvDone, tvPosted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        tvDone = findViewById(R.id.tvDone);
        tvPosted=findViewById(R.id.tvPosted);
        lvDone = findViewById(R.id.lvDone);
        lvPosted= findViewById(R.id.lvPosted);
        adapterDone = new ArrayAdapter<User>(this, R.layout.list_item, UsersData.getInstance().users);
        adapterPosted = new ArrayAdapter<User>(this, R.layout.list_item, UsersData.getInstance().usersPosted);
        lvDone.setAdapter(adapterDone);
        lvPosted.setAdapter(adapterPosted);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.ratings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ratings:
                        return true;
                    case R.id.job:
                        startActivity(new Intent(getApplicationContext(), JobsActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.new_job:
                        startActivity(new Intent(getApplicationContext(), NewJobActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
