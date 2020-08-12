package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.JobActivity;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mFirebaseAuth;
    public boolean open;

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
                      startActivity(new Intent(getApplicationContext(), JobActivity.class));
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE},2);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            mMap.setMyLocationEnabled(true);

            LatLng latLng = new LatLng(43.321443, 21.895592);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("User current location");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(markerOptions);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            showMyConnections();


        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    public void showMyConnections() {
        ArrayList<User> cons = new ArrayList<>();
        ArrayList<User> users = UsersData.getInstance().users;
        //String con = UsersData.getInstance().currentUser.connections.get(0);
        //Toast.makeText(this, con, Toast.LENGTH_LONG).show();

        /*ArrayList<User> conss = UsersData.getInstance().getUserConnections();
        for(int i=0; i<cons.size(); i++) {
            User con = cons.get(i);
            double lat = con.lat;
            double lng = con.lng;
            LatLng latLng = new LatLng(lat, lng);
            String name = con.firstName;

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(name);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(markerOptions);
        }*/
    }
}
