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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.ChangePasswordDialog;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    User currentUser;
    EditText etFirstName, etSurname, etEmail, etYears, etProfession;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Button btnEdit;
    DatabaseReference db;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_edit_profile);

        currentUser = UsersData.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseAuth = FirebaseAuth.getInstance();

        etFirstName = findViewById(R.id.etUserFirstName);
        etSurname = findViewById(R.id.etUserSurname);
        etEmail = findViewById(R.id.etUserEmail);
        etYears = findViewById(R.id.etUserYears);
        etProfession = findViewById(R.id.etUserProfession);
        etFirstName.setText(currentUser.getFirstName());
        etSurname.setText(currentUser.getLastName());
        etEmail.setText(currentUser.getEmail());
        btnEdit = findViewById(R.id.btnEdit);
        if (currentUser.getYears() > 0) {
            etYears.setText(String.valueOf(currentUser.getYears()));
        }
        if (currentUser.getProfession() != null) {
            etProfession.setText(currentUser.getProfession());
        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etSurname.getText().toString();
                String email = etEmail.getText().toString();
                String userYears = etYears.getText().toString();
                int years = Integer.parseInt(userYears);
                String profession = etProfession.getText().toString();
                if (firstName.isEmpty()) {
                    etFirstName.setError("Please enter first name.");
                    return;
                } else if (lastName.isEmpty()) {
                    etSurname.setError("Please enter surname.");
                    return;
                } else if (email.isEmpty()) {
                    etEmail.setError("Please enter email.");
                    return;
                }
                currentUser.setFirstName(firstName);
                currentUser.setLastName(lastName);
                currentUser.setEmail(email);
                currentUser.setYears(years);
                currentUser.setProfession(profession);
                mFirebaseAuth.getCurrentUser().updateEmail(email);

                DatabaseReference userRef = db.child(currentUser.getuID());
                userRef.setValue(currentUser);

                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
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
                i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_edit_profile:
                break;
            case R.id.nav_connections:
                i = new Intent(EditProfileActivity.this, ConnectionsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_job_history:
                i = new Intent(EditProfileActivity.this, JobHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                i = new Intent(EditProfileActivity.this, LoginActivity.class);
                startActivity(i);
                stopService(new Intent(EditProfileActivity.this, TrackingService.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
