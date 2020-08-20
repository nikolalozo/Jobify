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
    Button btnReset;
    Switch swJobs, swConnections;
    TextView tvSelectDate;
    private DatePickerDialog.OnDateSetListener selectDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        rsDistance=findViewById(R.id.rsDistance);
        rsPay=findViewById(R.id.rsPay);
        btnReset=findViewById(R.id.btnReset);
        swJobs=findViewById(R.id.swJobs);
        swConnections=findViewById(R.id.swConnections);
        tvSelectDate=findViewById(R.id.tvSelectDate);
        rsDistance.setValues(MapActivity.minDistance, MapActivity.maxDistance);
        rsPay.setValues(MapActivity.minPay.floatValue(), MapActivity.maxPay.floatValue());
        swJobs.setChecked(MapActivity.includeJobs);
        swConnections.setChecked(MapActivity.includeConnections);
        MapActivity.search=false;

        if(MapActivity.includeJobs) {
            tvSelectDate.setEnabled(true);
            rsPay.setEnabled(true);
        }
        else {
            tvSelectDate.setEnabled(false);
            rsPay.setEnabled(false);
        }

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

            }
        });

        swJobs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && !MapActivity.includeJobs) {
                    MapActivity.includeJobs = true;
                    tvSelectDate.setEnabled(true);
                    rsPay.setEnabled(true);
                    if(!MapActivity.includeConnections)
                        rsDistance.setEnabled(true);
                }
                else {
                    MapActivity.includeJobs = false;
                    tvSelectDate.setEnabled(false);
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

        tvSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = MapActivity.cal.get(Calendar.YEAR);
                int month = MapActivity.cal.get(Calendar.MONTH);
                int day = MapActivity.cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(FilterActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, selectDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        selectDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = month + "/" + dayOfMonth + "/" + year;
                tvSelectDate.setText(date);
                MapActivity.cal.set(year, month, dayOfMonth);
            }
        };
    }




}
