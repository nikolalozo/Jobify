package com.mosis.jobify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mosis.jobify.activities.UserRequestsActivity;
import com.mosis.jobify.models.Job;

import java.util.List;

public class JobListAdapter extends ArrayAdapter<Job> {
    private int layout;
    public Context context;
    private List<Job> mObjects;

    public JobListAdapter(Context context, int resource, List<Job> objects) {
        super(context, resource, objects);
        mObjects = objects;
        layout = resource;
        context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewholder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvJobInfo);
            viewHolder.button = (Button) convertView.findViewById(R.id.btnViewRequest);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(position).arrayIdRequested != null) {
                    Intent i = new Intent(v.getContext(), UserRequestsActivity.class);
                    i.putExtra("array", getItem(position).arrayIdRequested);
                    v.getContext().startActivity(i);
                } else {
                     Toast.makeText(v.getContext(), "There is no user requests", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mainViewholder.title.setText(getItem(position).getTitle());

        return convertView;
    }
}