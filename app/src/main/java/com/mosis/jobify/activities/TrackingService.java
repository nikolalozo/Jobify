package com.mosis.jobify.activities;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.mosis.jobify.StatusEnum.POSTED;
import static com.mosis.jobify.StatusEnum.REJECTED;
import static com.mosis.jobify.StatusEnum.TAKEN;


public class TrackingService extends Service {

    private DatabaseReference db;
    private DatabaseReference db1;
    private FirebaseAuth auth;

    private static final String TAG = "MyLocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 10f;
    public static boolean tracking=true;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    String NOTIFICATION_CHANNEL_ID = "1001";
    Date today;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            db.child("users").child(auth.getCurrentUser().getUid()).child("lat").setValue(mLastLocation.getLatitude());
            db.child("users").child(auth.getCurrentUser().getUid()).child("lng").setValue(mLastLocation.getLongitude());
                    sendNotificationJob();
                    sendNotificationConnection();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        today=Calendar.getInstance().getTime();
        db1=FirebaseDatabase.getInstance().getReference().child("jobs");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        Log.e(TAG, "onCreate");
        auth = FirebaseAuth.getInstance();
        db= FirebaseDatabase.getInstance().getReference();

        initializeLocationManager();

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist, " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[1]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist " + ex.getMessage());
        }

        tracking=true;

        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        sendNotificationRequest();
                    }
                };
                Handler handler = new android.os.Handler();
                handler.postDelayed(runnable, 2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendNotificationRequest() {
        ArrayList<Job> jobs = JobsData.getInstance().getJobs();
        User currentUser = UsersData.getInstance().getCurrentUser();
        for(int i=0; i<jobs.size(); i++) {
            Job job = jobs.get(i);
            if(job.idPosted.equals(currentUser.uID) && job.arrayIdRequested.size()>0 && job.status==POSTED)
            {
                Intent resultIntent = new Intent(TrackingService.this , JobsActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                        0 /* Request code */, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder = new NotificationCompat.Builder(TrackingService.this);
                mBuilder.setContentTitle("You have a new job request!")
                        .setContentText("Check it out!")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_work_black_24dp)
                        .setContentIntent(resultPendingIntent);

                mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
                assert mNotificationManager != null;
                mNotificationManager.notify(30 /* Request Code */, mBuilder.build());
            }

            if(job.idTaken!=null && job.status==TAKEN) {
                if (job.idTaken.equals(currentUser.uID)) {
                    Intent resultIntent = new Intent(TrackingService.this, JobsActivity.class);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                            0 /* Request code */, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder = new NotificationCompat.Builder(TrackingService.this);
                    mBuilder.setContentTitle("You've got work to do!")
                            .setContentText("See the list of jobs!")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_work_black_24dp)
                            .setContentIntent(resultPendingIntent);

                    mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        assert mNotificationManager != null;
                        mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                        mNotificationManager.createNotificationChannel(notificationChannel);
                    }
                    assert mNotificationManager != null;
                    mNotificationManager.notify(66 /* Request Code */, mBuilder.build());
                }
                if(job.idTaken.equals(currentUser.uID) && job.status==REJECTED)
                {
                    Intent resultIntent = new Intent(TrackingService.this, JobsActivity.class);
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                            0 /* Request code */, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    mBuilder = new NotificationCompat.Builder(TrackingService.this);
                    mBuilder.setContentTitle("Your job confirmation has been rejected.")
                            .setContentText("Your employer wasn't happy with you.")
                            .setAutoCancel(true)
                            .setSmallIcon(R.drawable.ic_work_black_24dp)
                            .setContentIntent(resultPendingIntent);

                    mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                        notificationChannel.enableLights(true);
                        notificationChannel.setLightColor(Color.RED);
                        notificationChannel.enableVibration(true);
                        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                        assert mNotificationManager != null;
                        mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                        mNotificationManager.createNotificationChannel(notificationChannel);
                    }
                    assert mNotificationManager != null;
                    mNotificationManager.notify(88 /* Request Code */, mBuilder.build());
                }
            }
        }
    }

    public void sendNotificationConnection() {
        ArrayList<User> userConnections = UsersData.getInstance().getUserConnections();
        User currentUser = UsersData.getInstance().getCurrentUser();
        for(int i=0; i<userConnections.size(); i++) {
            User con = userConnections.get(i);
            double distanceCon = MapActivity.pointsDistance(currentUser.lat, currentUser.lng, con.lat, con.lng);
            if(distanceCon<= 100) {
                Intent resultIntent = new Intent(TrackingService.this , MapActivity.class);
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                        0 /* Request code */, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder = new NotificationCompat.Builder(TrackingService.this);
                mBuilder.setContentTitle("You are closer than 100 meters to your friend!")
                        .setContentText("You can go to them and say hello!")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_person_outline_black_24dp)
                        .setContentIntent(resultPendingIntent);

                mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
                assert mNotificationManager != null;
                mNotificationManager.notify(10 /* Request Code */, mBuilder.build());

            }
        }
    }

    public void sendNotificationJob() {
        ArrayList<User> users = UsersData.getInstance().users;
        ArrayList<Job> jobs = JobsData.getInstance().getJobs();
        User currentUser = UsersData.getInstance().getCurrentUser();
        for(int i=0; i<users.size(); i++) {
            User user = users.get(i);
            for(int j=0; j<jobs.size(); j++) {
                Job job = jobs.get(j);
                double distanceJob = MapActivity.pointsDistance(currentUser.lat, currentUser.lng, job.latitude, job.longitude);
                double distanceUser = MapActivity.pointsDistance(currentUser.lat, currentUser.lng, user.lat, user.lng);
                if(distanceJob <= 100 && distanceUser<= 100 && job.idTaken!=null && job.status==TAKEN) {
                    if (job.idTaken.equals(currentUser.uID) && job.idPosted.equals(user.uID) && job.confirmedBy.size()==0) {
                        Intent resultIntent = new Intent(TrackingService.this, JobsActivity.class);
                        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                                0 /* Request code */, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        mBuilder = new NotificationCompat.Builder(TrackingService.this);
                        mBuilder.setContentTitle("You are closer than 100 meters to your employer!")
                                .setContentText("Click here to confirm that you've done your job!")
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.ic_work_black_24dp)
                                .setContentIntent(resultPendingIntent);

                        mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            assert mNotificationManager != null;
                            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }
                        assert mNotificationManager != null;
                        mNotificationManager.notify(15 /* Request Code */, mBuilder.build());
                    }
                    if (job.idTaken.equals(currentUser.uID) && job.idPosted.equals(user.uID) && job.confirmedBy.size()>0) {
                        if (!job.confirmedBy.get(0).equals(currentUser.uID)) {
                            Intent resultIntent = new Intent(TrackingService.this, JobsActivity.class);
                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                                    0 /* Request code */, resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            mBuilder = new NotificationCompat.Builder(TrackingService.this);
                            mBuilder.setContentTitle("You are closer than 100 meters to your employer!")
                                    .setContentText("Click here to confirm that you've done your job!")
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_work_black_24dp)
                                    .setContentIntent(resultPendingIntent);

                            mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                                notificationChannel.enableLights(true);
                                notificationChannel.setLightColor(Color.RED);
                                notificationChannel.enableVibration(true);
                                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                assert mNotificationManager != null;
                                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                                mNotificationManager.createNotificationChannel(notificationChannel);
                            }
                            assert mNotificationManager != null;
                            mNotificationManager.notify(15 /* Request Code */, mBuilder.build());
                        }
                    }

                    if (job.idTaken.equals(user.uID) && job.idPosted.equals(currentUser.uID) && job.confirmedBy.size()==0) {
                        Intent resultIntent = new Intent(TrackingService.this, JobsActivity.class);
                        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                                0 /* Request code */, resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                        mBuilder = new NotificationCompat.Builder(TrackingService.this);
                        mBuilder.setContentTitle("You are closer than 100 meters to your worker!")
                                .setContentText("Click here to confirm that they have done their job!")
                                .setAutoCancel(true)
                                .setSmallIcon(R.drawable.ic_work_black_24dp)
                                .setContentIntent(resultPendingIntent);

                        mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            int importance = NotificationManager.IMPORTANCE_HIGH;
                            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.enableVibration(true);
                            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                            assert mNotificationManager != null;
                            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }
                        assert mNotificationManager != null;
                        mNotificationManager.notify(15 /* Request Code */, mBuilder.build());
                    }
                    if (job.idTaken.equals(user.uID) && job.idPosted.equals(currentUser.uID) && job.confirmedBy.size()>0) {
                        if (!job.confirmedBy.get(0).equals(currentUser.uID)) {
                            Intent resultIntent = new Intent(TrackingService.this, JobsActivity.class);
                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            PendingIntent resultPendingIntent = PendingIntent.getActivity(TrackingService.this,
                                    0 /* Request code */, resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                            mBuilder = new NotificationCompat.Builder(TrackingService.this);
                            mBuilder.setContentTitle("You are closer than 100 meters to your worker!")
                                    .setContentText("Click here to confirm that they have done their job!")
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.drawable.ic_work_black_24dp)
                                    .setContentIntent(resultPendingIntent);

                            mNotificationManager = (NotificationManager) TrackingService.this.getSystemService(Context.NOTIFICATION_SERVICE);

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                int importance = NotificationManager.IMPORTANCE_HIGH;
                                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                                notificationChannel.enableLights(true);
                                notificationChannel.setLightColor(Color.RED);
                                notificationChannel.enableVibration(true);
                                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                                assert mNotificationManager != null;
                                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                                mNotificationManager.createNotificationChannel(notificationChannel);
                            }
                            assert mNotificationManager != null;
                            mNotificationManager.notify(15 /* Request Code */, mBuilder.build());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                    tracking=false;
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: "+ LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(Notification.PRIORITY_MIN)
                .build();
        startForeground(2, notification);
    }

}