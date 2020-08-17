package com.mosis.jobify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserListAdapter extends ArrayAdapter<User> {
    private int layout;
    public Context context;
    private List<User> mObjects;
    public Job job;
    private DatabaseReference db;

    public UserListAdapter(Context contextt, int resource, List<User> objects, Job jobb) {
        super(contextt, resource, objects);
        db = FirebaseDatabase.getInstance().getReference("jobs");
        mObjects = objects;
        layout = resource;
        context = contextt;
        job = jobb;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mainViewHolder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvUserInfo);
            viewHolder.button = (Button) convertView.findViewById(R.id.btnAcceptRequest);
            convertView.setTag(viewHolder);
        }
        mainViewHolder = (ViewHolder) convertView.getTag();
        mainViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference jobRef = db.child(job.getKey());
                Map<String, Object> jobUpdates = new HashMap<>();
                jobUpdates.put("idTaken", getItem(position).getuID());
                jobUpdates.put("status", "TAKEN");
                jobRef.updateChildren(jobUpdates);


            }
        });

        mainViewHolder.title.setText(getItem(position).fullName());

        return convertView;
    }
}
