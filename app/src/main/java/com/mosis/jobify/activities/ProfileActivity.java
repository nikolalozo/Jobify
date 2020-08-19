package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.JobsActivity;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mFirebaseAuth;
    User user;
    TextView tvFullName, tvConnections;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvFullName = findViewById(R.id.tvFullName);
        tvConnections = findViewById(R.id.numConnections);

        ImageView imageView = findViewById(R.id.profileImage);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ema);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        mFirebaseAuth = FirebaseAuth.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (User) extras.get("user");
        } else {
            user = UsersData.getInstance().getCurrentUser();
        }

        tvFullName.setText(user.fullName());
        if (user.getConnections() != null) {
            tvConnections.setText(String.valueOf(user.getConnections().size()));
        } else {
            tvConnections.setText("0");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
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
            case R.id.nav_edit_profile:
                i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_connections:
                i = new Intent(ProfileActivity.this, ConnectionsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_job_history:
                i = new Intent(ProfileActivity.this, JobHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.nav_turn_off:
                stopService(new Intent(ProfileActivity.this, TrackingService.class));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
                stopService(new Intent(ProfileActivity.this, TrackingService.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
