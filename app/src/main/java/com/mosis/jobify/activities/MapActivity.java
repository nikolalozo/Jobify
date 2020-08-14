package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.JobsActivity;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mFirebaseAuth;
    public boolean open;
    ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mFirebaseAuth=FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()) {
                  case R.id.profile:
                      startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            mMap.setMyLocationEnabled(true);

            LatLng latLng = new LatLng(43.321443, 21.895592);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            showMyConnections();
            showAllJobs();
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
            {
                @Override
                public void onInfoWindowClick(final Marker marker) {
                    if (marker != null) {
                        Object obj = marker.getTag();
                        Bundle bundle = new Bundle();
                        Intent intent1;
                        if (obj instanceof User) {
                            bundle.putSerializable("user", (User) obj);
                            intent1 = new Intent(MapActivity.this, ProfileActivity.class);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                        } else {
                            bundle.putSerializable("job", (Job) obj);
                            intent1 = new Intent(MapActivity.this, JobActivity.class);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                        }
                    }}
            });
        }

    public void showMyConnections() {
        for(int i=0; i<UsersData.getInstance().getUserConnections().size(); i++) {
            User con = UsersData.getInstance().getUserConnections().get(i);
            double lat = con.lat;
            double lng = con.lng;
            LatLng latLng = new LatLng(lat, lng);
            String name = con.firstName;

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(con);
            markers.add(marker);
        }
    }

    public void showAllJobs() {
        final ArrayList<Job> overlayArrayList = new ArrayList<>();
        for (int i = 0; i < JobsData.getInstance().getJobs().size(); i++) {
            Job job = JobsData.getInstance().getJob(i);
            double lat = job.getLatitude();
            double lng = job.getLongitude();
            LatLng latLng = new LatLng(lat, lng);
            String title = job.getTitle();
            job.setKey(JobsData.getInstance().getJob(i).getKey());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(title);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(job);
            markers.add(marker);
        }
    }
}
