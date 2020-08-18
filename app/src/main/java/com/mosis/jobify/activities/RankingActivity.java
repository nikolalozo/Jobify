package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mosis.jobify.CurrentJobsFragment;
import com.mosis.jobify.DoneJobsRankingListFragment;
import com.mosis.jobify.JobRequestsFragment;
import com.mosis.jobify.JobsActivity;
import com.mosis.jobify.PendingJobsFragment;
import com.mosis.jobify.PostedJobsRankingListFragment;
import com.mosis.jobify.R;
import com.mosis.jobify.SectionsPagerAdapter;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

public class RankingActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        getSupportActionBar().hide();

        mViewPager = (ViewPager) findViewById(R.id.containerRanking);
        setupViewPager(mViewPager);
        mSectionsPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsRanking);
        tabLayout.setupWithViewPager(mViewPager);

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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PostedJobsRankingListFragment(), "Posted jobs");
        adapter.addFragment(new DoneJobsRankingListFragment(), "Done jobs");
        viewPager.setAdapter(adapter);
    }
}
