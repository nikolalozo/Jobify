package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.R;
import com.mosis.jobify.StatusEnum;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mFirebaseAuth;
    private FloatingActionButton fabFilter, fabSearch, fabZoomIn, fabZoomOut;
    static Integer minPay, maxPay;
    static float minDistance, maxDistance;
    static boolean includeJobs, includeConnections;
    static Date applyBy;
    public static Calendar cal;
    Date today;
    private StorageReference st;
    private DatabaseReference db;
    public static double newLat;
    public static double newLng;
    public static boolean search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mFirebaseAuth = FirebaseAuth.getInstance();
        st= FirebaseStorage.getInstance().getReference();
        db= FirebaseDatabase.getInstance().getReference().child("users");
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);
        fabFilter = findViewById(R.id.fabFilter);
        fabSearch=findViewById(R.id.fabSearch);
        fabZoomIn=findViewById(R.id.fabZoomIn);
        fabZoomOut=findViewById(R.id.fabZoomOut);
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

        fabZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        fabZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, SearchActivity.class));
            }
        });

        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapActivity.this, FilterActivity.class));
            }
        });

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        mMap.clear();
                        if(includeConnections)
                            showMyConnections();
                        if(includeJobs)
                            showJobs();
                        //sendNotificationJob();
                        //if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
                        //sendNotificationConnection();
                    }
                };
                Handler handler = new android.os.Handler();
                handler.postDelayed(runnable, 2500);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
        {
            @Override
            public void onMapLoaded()
            {
                if (includeJobs)
                    showJobs();
                if (includeConnections)
                    showMyConnections();
            }
        });
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
        if(search) {
            LatLng latLng = new LatLng(newLat, newLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }

    private Bitmap createUserBitmap(Bitmap image) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(62), dp(76), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.drawable.livepin);
            drawable.setBounds(0, 0, dp(62), dp(76));
            drawable.draw(canvas);

            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();

            Bitmap bitmap = image;
            //Bitmap bitmap = BitmapFactory.decodeFile(path.toString()); /*generate bitmap here if your image comes from any url*/
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(52) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(5), dp(5));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(5), dp(5), dp(52 + 5), dp(52 + 5));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }


    public void showMyConnections() {
        ArrayList<User> userConnections=UsersData.getInstance().getUserConnections();
        for (int i = 0; i < userConnections.size(); i++) {
            final User con = userConnections.get(i);
            final User currentUser = UsersData.getInstance().getCurrentUser();
            double distance = pointsDistance(currentUser.lat, currentUser.lng, con.lat, con.lng);
            if (distance > minDistance && distance < maxDistance) {
                double lat = con.lat;
                double lng = con.lng;
                final LatLng latLng = new LatLng(lat, lng);

                st.child("users").child(con.uID).child("picture").getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Bitmap scaledBmp = bitmap.createScaledBitmap(bitmap, 256, 256, false);
                        Bitmap userBitmap = createUserBitmap(scaledBmp);
                        if (userBitmap != null) {
                            mMap.addMarker(
                                    new MarkerOptions()
                                            .position(latLng)
                                            .icon(BitmapDescriptorFactory.fromBitmap(userBitmap)).anchor(0.5f, 1));
                        }
                    }
                });
            }
        }
    }


    public void showJobs() {
        ArrayList<Job> jobs = JobsData.getInstance().getJobs();
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            User currentUser = UsersData.getInstance().getCurrentUser();
            double distance = pointsDistance(currentUser.lat, currentUser.lng, job.latitude, job.longitude);
            if(job.wage>=minPay && job.wage<=maxPay && distance>=minDistance && distance<=maxDistance && job.status== StatusEnum.POSTED) {
                double lat = job.getLatitude();
                double lng = job.getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                String title = job.getTitle();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(title);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.briefcase));

                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(job);
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
