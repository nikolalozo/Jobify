package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.ConnectionAdapter;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class ConnectionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public ArrayList<User> connections;
    ListView lvConnections;
    TextView tvNoConnections;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbarProfile);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_connections);

        tvNoConnections = findViewById(R.id.tvNoConnections);

        connections = UsersData.getInstance().getUserConnections();
        lvConnections = findViewById((R.id.lvConnections));
        lvConnections.setAdapter(new ConnectionAdapter(this, connections));
        if (connections.size() > 0) {
            tvNoConnections.setVisibility(View.INVISIBLE);
        }


        lvConnections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", (User) connections.get(position));
                Intent i = new Intent(ConnectionsActivity.this, ProfileActivity.class);
                i.putExtras(bundle);
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
                i = new Intent(ConnectionsActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_edit_profile:
                i = new Intent(ConnectionsActivity.this, EditProfileActivity.class);
                startActivity(i);
                break;
            case R.id.nav_connections:
                break;
            case R.id.nav_job_history:
                i = new Intent(ConnectionsActivity.this, JobHistoryActivity.class);
                startActivity(i);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                i = new Intent(ConnectionsActivity.this, LoginActivity.class);
                startActivity(i);
                stopService(new Intent(ConnectionsActivity.this, TrackingService.class));
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
