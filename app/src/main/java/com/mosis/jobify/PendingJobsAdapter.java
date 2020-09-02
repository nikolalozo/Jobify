package com.mosis.jobify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mosis.jobify.models.Job;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class PendingJobsAdapter extends ArrayAdapter<Job> {
    Context context;
    ArrayList<Job> jobs;

    public PendingJobsAdapter(Context c, ArrayList<Job> jobsArray) {
        super(c, R.layout.layout_pending_job_list_item, jobsArray);
        this.context = c;
        this.jobs = jobsArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.layout_pending_job_list_item, parent, false);
        TextView jobTitle = row.findViewById(R.id.tvJobInfo);
        TextView jobDate = row.findViewById(R.id.tvJobDate);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

        jobTitle.setText(getItem(position).getTitle());
        jobDate.setText(dateFormat.format(getItem(position).getDate()));

        return row;
    }
}
