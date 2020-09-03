package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.mosis.jobify.CustomInfoDialog;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
    Job job;
    String jobTitle, pay;
    int jobPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        getSupportActionBar().setTitle("Add new job");
        openDialog();
        mFirebaseAuth=FirebaseAuth.getInstance();
        etJobTitle = findViewById(R.id.etJobTitle);
        etJobPay = findViewById(R.id.etPay);
        btnNext = findViewById(R.id.btnNext);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

        job = new Job();
        User currentUser = UsersData.getInstance().getCurrentUser();
        job.setLatitude(currentUser.getLat());
        job.setLongitude(currentUser.getLng());
        job.setIdPosted(currentUser.getuID());
        btnNext.setEnabled(false);
        etJobPay.setText(Integer.toString(job.getWage()));
        pay = etJobPay.getText().toString();
        etJobTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)  {
                if (charSequence.length() > 0 && Integer.valueOf(etJobPay.getText().toString()) > 0) {
                    btnNext.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && Integer.valueOf(etJobPay.getText().toString()) > 0) {
                    btnNext.setEnabled(true);
                } else {
                    btnNext.setEnabled(false);
                }
            }
        });

        etJobPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count)  {
                if (charSequence.length() > 0 && !etJobTitle.getText().toString().isEmpty()) {
                    btnNext.setEnabled(true);
                    jobPay = Integer.parseInt(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0 && !etJobTitle.getText().toString().isEmpty()) {
                    btnNext.setEnabled(true);
                    jobPay = Integer.parseInt(editable.toString());
                } else {
                    btnNext.setEnabled(false);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                job.setTitle(etJobTitle.getText().toString());
                job.setWage(jobPay);
                if (etJobTitle.getText().toString().isEmpty()) {
                    etJobTitle.setError("Please enter job title.");
                    Toast.makeText(NewJobActivity.this, "Please enter job title.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(NewJobActivity.this, NewJobDescriptionActivity.class);
                    i.putExtra("job", job);
                    startActivity(i);
                }
            }
        });
        mDisplayDate = (TextView) findViewById(R.id.tvSelectedDate);
        mDisplayDate.setText(dateFormat.format(job.getDate()));
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
                job.setDate(new Date(year - 1900, month - 1, dayOfMonth));
                mDisplayDate.setText(date);
            }
        };

        mDisplayHour = (TextView) findViewById(R.id.tvStartedHour);
        mDisplayHour.setText(timeFormat.format(job.getDate()));
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
        mDisplayApplyByDate.setText(dateFormat.format(job.getAppliedBy()));
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
                job.setAppliedBy(new Date(year - 1900, month - 1, dayOfMonth));
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
                        startActivity(new Intent(getApplicationContext(), JobsActivity.class));
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
        CustomInfoDialog newJobDialog = new CustomInfoDialog();
        newJobDialog.setMessage("Location of job you want to added is your current location.");
        newJobDialog.show(getSupportFragmentManager(), "new job dialog");
    }
}
