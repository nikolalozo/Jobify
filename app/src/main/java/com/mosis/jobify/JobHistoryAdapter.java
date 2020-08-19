package com.mosis.jobify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class JobHistoryAdapter extends ArrayAdapter<Job> {

    Context context;
    ArrayList<Job> doneJobs;

    public JobHistoryAdapter(Context c, ArrayList<Job> doneJobsArray) {
        super(c, R.layout.row_job_history, doneJobsArray);
        this.context = c;
        this.doneJobs = doneJobsArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_job_history, parent, false);
        TextView jobTitle = row.findViewById(R.id.tvDoneJobTitle);
        TextView jobDate = row.findViewById(R.id.tvDoneJobDate);
        TextView jobOwner = row.findViewById(R.id.tvDoneJobOwner);

        jobTitle.setText(getItem(position).getTitle());
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        jobDate.setText(dateFormat.format(getItem(position).getDate()));
        jobOwner.setText(UsersData.getInstance().getUser(getItem(position).getIdPosted()).fullName());
        jobOwner.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_all, 0, 0, 0);

        return row;
    }
}
