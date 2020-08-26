package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.CustomInfoDialog;
import com.mosis.jobify.DoneJobsHistoryFragment;
import com.mosis.jobify.PostedJobsHistoryFragment;
import com.mosis.jobify.R;
import com.mosis.jobify.SectionsPagerAdapter;

public class JobHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SectionsPagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_history);
        openDialog();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_job_history);

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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent i;
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                i = new Intent(JobHistoryActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_edit_profile:
                i = new Intent(JobHistoryActivity.this, EditProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_connections:
                i = new Intent(JobHistoryActivity.this, ConnectionsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_job_history:
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                i = new Intent(JobHistoryActivity.this, LoginActivity.class);
                startActivity(i);
                stopService(new Intent(JobHistoryActivity.this, TrackingService.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
