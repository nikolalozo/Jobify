package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.JobsActivity;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mFirebaseAuth;
    public boolean open;
    ArrayList<Marker> markers = new ArrayList<Marker>();
    private FloatingActionButton fabFilter;
    static Integer minPay, maxPay;
    static float minDistance, maxDistance;
    static boolean includeJobs, includeConnections;
    static Date applyBy;
    public static Calendar cal;
    Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mFirebaseAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);
        fabFilter = findViewById(R.id.fabFilter);
        minDistance = 0;
        maxDistance = 6000;
        minPay = 100;
        maxPay = 3000;
        includeConnections = true;
        includeJobs = true;
        cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 1);
        applyBy=cal.getTime();
        today=Calendar.getInstance().getTime();

        Toast.makeText(this, String.valueOf(applyBy), Toast.LENGTH_LONG).show();


        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, SearchActivity.class));
            }
        });

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

        if (includeConnections)
            showMyConnections();
        if (includeJobs)
            showJobs();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
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
                }
            }
        });

        //Toast.makeText(this, String.valueOf(JobsData.getInstance().getJobs().get(0).appliedBy), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMap.clear();
        applyBy=cal.getTime();
        if (includeConnections)
            showMyConnections();
        if (includeJobs)
            showJobs();
        //Toast.makeText(this, String.valueOf(applyBy.before(JobsData.getInstance().getJob(0).appliedBy)), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, String.valueOf(JobsData.getInstance().getJob(0).appliedBy), Toast.LENGTH_SHORT).show();
    }


    public void showMyConnections() {
        for(int i=0; i<UsersData.getInstance().getUserConnections().size(); i++) {
            User con = UsersData.getInstance().getUserConnections().get(i);
            User currentUser = UsersData.getInstance().getCurrentUser();
            double distance = pointsDistance(currentUser.lat, currentUser.lng, con.lat, con.lng);
            if(distance>minDistance && distance<maxDistance) {
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
    }

    public void showJobs() {
        for (int i = 0; i < JobsData.getInstance().getJobs().size(); i++) {
            Job job = JobsData.getInstance().getJob(i);
            User currentUser = UsersData.getInstance().getCurrentUser();
            double distance = pointsDistance(currentUser.lat, currentUser.lng, job.latitude, job.longitude);
            if(job.wage>minPay && job.wage<maxPay && distance>minDistance && distance<maxDistance) {
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

    public static double pointsDistance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6378137.0; // Earth’s mean radius in meter
        double dLat = rad(lat2-lat1);
        double dLng = rad(lng2-lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(rad(lat1)) * Math.cos(rad(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d; // returns the distance in meter
    }

    public static double rad(double x) {
        return x * Math.PI / 180;
    }
}
