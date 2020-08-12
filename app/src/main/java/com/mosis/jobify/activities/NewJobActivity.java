package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.JobActivity;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NewJobActivity extends AppCompatActivity {
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayHour;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private TextView mDisplayApplyByDate;
    private DatePickerDialog.OnDateSetListener mApplyByDateSetListener;
    FirebaseAuth mFirebaseAuth;
    Button btnNext;
    EditText etJobTitle, etJobPay;
    TextView tvUID;
    Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        openDialog();
        mFirebaseAuth=FirebaseAuth.getInstance();
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobPay = findViewById(R.id.etPay);
        btnNext = findViewById(R.id.btnNext);
        tvUID = findViewById(R.id.tvUID);

        job = new Job();

        User currentUser = UsersData.getInstance().getCurrentUser();
        job.setLatitude(currentUser.getLat());
        job.setLongitude(currentUser.getLng());
        job.setIdPosted(currentUser.getuID());

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jobTitle = etJobTitle.getText().toString();
                String pay = etJobPay.getText().toString();
                job.setTitle(jobTitle);
                int jobPay = Integer.parseInt(pay);
                job.setWage(jobPay);
                if (jobTitle.isEmpty()) {
                    etJobTitle.setError("Please enter job title.");
                    return;
                } else if (jobPay == 0) {
                    etJobPay.setError("Please enter pay.");
                    return;
                }
                Intent i = new Intent(NewJobActivity.this, NewJobDescriptionActivity.class);
                i.putExtra("job", job);
                startActivity(i);
            }
        });
        mDisplayDate = (TextView) findViewById(R.id.tvSelectedDate);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewJobActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                job.setDate(new Date(year, month, dayOfMonth));
                mDisplayDate.setText(date);
            }
        };

        mDisplayHour = (TextView) findViewById(R.id.tvStartedHour);
        mDisplayHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(NewJobActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String hour = hourOfDay + ":" + minute;
                if (job.getDate() != null) {
                    Date date = job.getDate();
                    date.setHours(hourOfDay);
                    date.setMinutes(minute);
                    job.setDate(date);
                }
                mDisplayHour.setText(hour);
            }
        };

        mDisplayApplyByDate = (TextView) findViewById(R.id.tvApplyByDate);
        mDisplayApplyByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(NewJobActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mApplyByDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mApplyByDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                job.setAppliedBy(new Date(year, month, dayOfMonth));
                mDisplayApplyByDate.setText(date);
            }
        };


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.new_job);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
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
                        startActivity(new Intent(getApplicationContext(), JobActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.new_job:
                        return true;
                }
                return false;
            }
        });
    }

    public void openDialog() {
        NewJobDialog newJobDialog = new NewJobDialog();
        newJobDialog.show(getSupportFragmentManager(), "new job dialog");

    }
}
