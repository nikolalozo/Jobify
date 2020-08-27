package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.RangeSlider;
import com.mosis.jobify.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    RangeSlider rsDistance;
    RangeSlider rsPay;
    Button btnReset, btnSet;
    Switch swJobs, swConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        rsDistance=findViewById(R.id.rsDistance);
        rsPay=findViewById(R.id.rsPay);
        btnReset=findViewById(R.id.btnReset);
        btnSet=findViewById(R.id.btnSet);
        swJobs=findViewById(R.id.swJobs);
        swConnections=findViewById(R.id.swConnections);
        rsDistance.setValues(MapActivity.minDistance, MapActivity.maxDistance);
        rsPay.setValues(MapActivity.minPay.floatValue(), MapActivity.maxPay.floatValue());
        swJobs.setChecked(MapActivity.includeJobs);
        swConnections.setChecked(MapActivity.includeConnections);
        MapActivity.search=false;

        rsDistance.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> list = slider.getValues();
                MapActivity.minDistance=list.get(0);
                MapActivity.maxDistance=list.get(1);
            }
        });

        rsPay.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> list = slider.getValues();
                MapActivity.minPay=list.get(0).intValue();
                MapActivity.maxPay=list.get(1).intValue();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer i = 0;
                Integer j =6000;
                Integer k = 100;
                Integer l = 3000;
                rsDistance.setValues(i.floatValue(), j.floatValue());
                rsPay.setValues(k.floatValue(), l.floatValue());
                MapActivity.minDistance=0;
                MapActivity.maxDistance=6000;
                MapActivity.minPay=100;
                MapActivity.maxPay=3000;
                MapActivity.cal=Calendar.getInstance();
                MapActivity.cal.add(Calendar.MONTH, 1);
                if(!MapActivity.includeJobs) {
                    swJobs.setChecked(true);
                }
                if(!MapActivity.includeConnections) {
                    swConnections.setChecked(true);
                }

            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swJobs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !MapActivity.includeJobs) {
                    MapActivity.includeJobs = true;
                    rsPay.setEnabled(true);
                    if(!MapActivity.includeConnections)
                        rsDistance.setEnabled(true);
                }
                else {
                    MapActivity.includeJobs = false;
                    rsPay.setEnabled(false);
                }
            }
        });

        swConnections.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !MapActivity.includeConnections) {
                    MapActivity.includeConnections = true;
                    if(!MapActivity.includeJobs)
                        rsDistance.setEnabled(true);
                }
                else {
                    MapActivity.includeConnections = false;
                    if(!MapActivity.includeJobs)
                        rsDistance.setEnabled(false);
                }
            }
        });

    }




}
