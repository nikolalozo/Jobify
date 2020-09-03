package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mFirebaseAuth;
    User user;
    TextView tvFullName, tvConnections, tvDoneJobs, tvYears, tvProfession, tvMark;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    StorageReference st;
    Switch swService;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvFullName = findViewById(R.id.tvFullName);
        tvConnections = findViewById(R.id.numConnections);
        tvDoneJobs = findViewById(R.id.numDoneJobs);
        tvYears = findViewById(R.id.tvYears);
        tvProfession = findViewById(R.id.tvProfession);
        tvMark = findViewById(R.id.tvMark);

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setIsIndicator(true);

        st = FirebaseStorage.getInstance().getReference();
        final ImageView imageView = (CircleImageView) findViewById(R.id.profile_picture);
        swService = findViewById(R.id.swService);
        if(TrackingService.tracking)
        swService.setChecked(true);
        else
            swService.setChecked(false);

        Bundle extras = getIntent().getExtras();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        if (extras != null) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.bringToFront();
            navigationView.setCheckedItem(R.id.nav_profile);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        if (extras != null) {
            user = (User) extras.get("user");
        } else {
            user = UsersData.getInstance().getCurrentUser();
        }
        if (!user.uID.equals(UsersData.getInstance().getCurrentUser().getuID())) {
            swService.setVisibility(View.INVISIBLE);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) findViewById(R.id.scrollView2).getLayoutParams();
            layoutParams.setMargins(0,0,0,0);
        } else {
            swService.setVisibility(View.VISIBLE);
        }
        float mark = JobsData.getInstance().getAverageNoteForUser(user.getuID());
        ratingBar.setRating(mark);
        tvMark.setText(mark + "/5");

        st.child("users").child(user.getuID()).child("picture").getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap scaledBmp = bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), false);
                imageView.setImageBitmap(scaledBmp);
            }
        });

        tvFullName.setText(user.fullName());
        tvDoneJobs.setText(String.valueOf(user.getJobsDone()));
        if (user.getYears() > 0) {
            tvYears.setText(String.valueOf(user.getYears()) + " years old");
        }
        if (user.getProfession() != null) {
            tvProfession.setText(user.getProfession());
        }
        if (user.getConnections() != null) {
            tvConnections.setText(String.valueOf(user.getConnections().size()));
        } else {
            tvConnections.setText("0");
        }

        swService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !TrackingService.tracking) {
                    startService(new Intent(ProfileActivity.this, TrackingService.class));
                    TrackingService.tracking=true;
                }
                else
                    stopService(new Intent(ProfileActivity.this, TrackingService.class));
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (extras != null) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }
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
            case R.id.nav_profile:
                break;
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
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                stopService(new Intent(ProfileActivity.this, TrackingService.class));
                i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
                finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
