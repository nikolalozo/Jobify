package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.mosis.jobify.CustomInfoDialog;
import com.mosis.jobify.DoneJobsHistoryFragment;
import com.mosis.jobify.PostedJobsHistoryFragment;
import com.mosis.jobify.R;
import com.mosis.jobify.SectionsPagerAdapter;

public class JobHistoryActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history);
        openDialog();
        getSupportActionBar().hide();

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        mSectionsPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DoneJobsHistoryFragment(), "Done jobs");
        adapter.addFragment(new PostedJobsHistoryFragment(), "Posted jobs");
        viewPager.setAdapter(adapter);
    }

    public void openDialog() {
        CustomInfoDialog infoJobDialog = new CustomInfoDialog();
        infoJobDialog.setMessage("After the job had been done, you and other person are connected on Jobify. Please take a moment and rate each other.");
        infoJobDialog.setTitle("Success!");
        infoJobDialog.show(getSupportFragmentManager(), "confirm job dialog");
    }
}
