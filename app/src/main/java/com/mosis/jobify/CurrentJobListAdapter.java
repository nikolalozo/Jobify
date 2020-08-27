package com.mosis.jobify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.activities.MapActivity;
import com.mosis.jobify.activities.ProfileActivity;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentJobListAdapter extends ArrayAdapter<Job> {
    private int layout;
    public Context context;
    private List<Job> mObjects;
    private DatabaseReference db;
    private DatabaseReference dbUsers;
    MapActivity mapActivity;

    public CurrentJobListAdapter(Context cont, int resource, List<Job> objects) {
        super(cont, resource, objects);
        mObjects = objects;
        layout = resource;
        context = cont;
        db = FirebaseDatabase.getInstance().getReference("jobs");
        dbUsers = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        JobViewHolder mainViewholder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            JobViewHolder viewHolder = new JobViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvJobName);
            viewHolder.fullName = (TextView) convertView.findViewById(R.id.tvUserJobTaken);
            viewHolder.button = (Button) convertView.findViewById(R.id.btnConfirmJob);
            viewHolder.tv2 = (TextView) convertView.findViewById(R.id.tvViewProfile);
            viewHolder.acceptButton = (Button) convertView.findViewById(R.id.btnAcceptJob);
            viewHolder.declineButton = (Button) convertView.findViewById(R.id.btnDeclineJob);
            convertView.setTag(viewHolder);
        }
        final User currentUser = UsersData.getInstance().getCurrentUser();
        final User userJobTaken = UsersData.getInstance().getUser(getItem(position).getIdTaken());
        final User userJobPosted = UsersData.getInstance().getUser(getItem(position).getIdPosted());

        mainViewholder = (JobViewHolder) convertView.getTag();
        mainViewholder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Job job = getItem(position);
                job.confirmRequest(UsersData.getInstance().getCurrentUser().getuID());
                DatabaseReference jobRef = db.child(job.getKey());
                Map<String, Object> jobUpdates = new HashMap<>();
                jobUpdates.put("confirmedBy", job.getConfirmedBy());

                jobRef.updateChildren(jobUpdates);

                remove(getItem(position));
            }
        });

        mainViewholder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Job job = getItem(position);
                job.confirmRequest(UsersData.getInstance().getCurrentUser().getuID());
                DatabaseReference jobRef = db.child(job.getKey());
                Map<String, Object> jobUpdates = new HashMap<>();
                jobUpdates.put("confirmedBy", job.getConfirmedBy());
                jobUpdates.put("status", StatusEnum.DONE);
                jobRef.updateChildren(jobUpdates);
                DatabaseReference usersRef;
                Map<String, Object> userUpdates;

                CustomInfoDialog customInfoDialog = new CustomInfoDialog();

                if (!userJobTaken.getConnections().contains(userJobPosted.getuID())) {
                    userJobTaken.addConnection(userJobPosted.getuID());
                    usersRef = dbUsers.child(userJobTaken.getuID());
                    userUpdates = new HashMap<>();
                    userUpdates.put("connections", userJobTaken.getConnections());
                    usersRef.updateChildren(userUpdates);
                    customInfoDialog.setMessage("Congratulations! You finished the job and become friend with" + userJobPosted.fullName());
                    customInfoDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "custom info dialog");
                } else {
                    customInfoDialog.setMessage("Congratulations! You finished the job");
                    customInfoDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "custom info dialog");
                }
                usersRef = dbUsers.child(userJobTaken.getuID());
                userJobTaken.incrementJobsDone();
                userUpdates = new HashMap<>();
                userUpdates.put("jobsDone", userJobTaken.getJobsDone());
                usersRef.updateChildren(userUpdates);

                if (!userJobPosted.getConnections().contains(userJobTaken.getuID())) {
                    userJobPosted.addConnection(userJobTaken.getuID());
                    usersRef = dbUsers.child(userJobPosted.getuID());
                    userUpdates = new HashMap<>();
                    userUpdates.put("connections", userJobPosted.getConnections());
                    usersRef.updateChildren(userUpdates);
                }
                remove(getItem(position));
            }
        });

        mainViewholder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItem(position).status = StatusEnum.REJECTED;
                remove(getItem(position));
                CustomInfoDialog customInfoDialog = new CustomInfoDialog();
                customInfoDialog.setMessage("You rejected the job.");
                customInfoDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "custom info dialog");
            }
        });

        mainViewholder.tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                if (getItem(position).getIdPosted().equals(UsersData.getInstance().getCurrentUser().getuID())) {
                    bundle.putSerializable("user", (User) UsersData.getInstance().getUser(getItem(position).getIdTaken()));
                } else {
                    bundle.putSerializable("user", (User) UsersData.getInstance().getUser(getItem(position).getIdPosted()));
                }
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                i.putExtras(bundle);
                v.getContext().startActivity(i);
            }
        });

        double distance = mapActivity.pointsDistance(currentUser.getLat(), currentUser.getLng(), userJobTaken.getLat(), userJobTaken.getLng());
        if (distance > 100) {
            mainViewholder.button.setVisibility(View.INVISIBLE);
            mainViewholder.acceptButton.setVisibility(View.INVISIBLE);
            mainViewholder.declineButton.setVisibility(View.INVISIBLE);
        } else if (getItem(position).getConfirmedBy().size() > 0) {
            if (getItem(position).getConfirmedBy().contains(currentUser.getuID())) {
                mainViewholder.acceptButton.setVisibility(View.INVISIBLE);
                mainViewholder.declineButton.setVisibility(View.INVISIBLE);
            }
            mainViewholder.button.setVisibility(View.INVISIBLE);
        } else {
            mainViewholder.acceptButton.setVisibility(View.INVISIBLE);
            mainViewholder.declineButton.setVisibility(View.INVISIBLE);
        }

        mainViewholder.title.setText(getItem(position).getTitle());
        if (getItem(position).getIdPosted().equals(UsersData.getInstance().getCurrentUser().getuID())) {
            mainViewholder.fullName.setText(userJobTaken.fullName());
        } else {
            mainViewholder.fullName.setText(userJobPosted.fullName());
        }

        return convertView;
    }
}
