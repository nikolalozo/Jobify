package com.mosis.jobify;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mosis.jobify.activities.MapActivity;
import com.mosis.jobify.activities.ProfileActivity;
import com.mosis.jobify.activities.RankingActivity;

public class JobsActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        mSectionsPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.job);
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
                        startActivity(new Intent(getApplicationContext(), RankingActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.job:
                        return true;
                }
                return false;
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PendingJobsFragment(), "Pending jobs");
        adapter.addFragment(new JobRequestsFragment(), "Job requests");
        viewPager.setAdapter(adapter);
    }
}