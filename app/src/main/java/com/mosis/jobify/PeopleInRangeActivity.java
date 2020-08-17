package com.mosis.jobify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PeopleInRangeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_in_range);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("People in range");
    }
}
