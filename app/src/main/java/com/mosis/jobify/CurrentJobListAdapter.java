package com.mosis.jobify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.mosis.jobify.activities.ConfirmJobDialog;
import com.mosis.jobify.models.Job;

import java.util.List;

public class CurrentJobListAdapter extends ArrayAdapter<Job> {
    private int layout;
    public Context context;
    private List<Job> mObjects;

    public CurrentJobListAdapter(Context cont, int resource, List<Job> objects) {
        super(cont, resource, objects);
        mObjects = objects;
        layout = resource;
        context = cont;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder mainViewholder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvJobInfo);
            viewHolder.button = (Button) convertView.findViewById(R.id.btnConfirmJob);
            convertView.setTag(viewHolder);
        }
        mainViewholder = (ViewHolder) convertView.getTag();
        mainViewholder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmJobDialog confirmJobDialog = new ConfirmJobDialog();
                confirmJobDialog.show(((JobsActivity)context).getSupportFragmentManager(), "confirm job dialog");
            }
        });
        mainViewholder.title.setText(getItem(position).getTitle());

        return convertView;
    }
}
