package com.mosis.jobify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.activities.ConnectionsActivity;
import com.mosis.jobify.activities.JobsActivity;
import com.mosis.jobify.activities.ProfileActivity;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends ArrayAdapter<User> {
    private int layout;
    public Context context;
    private List<User> mObjects;
    public Job job;
    private DatabaseReference db;
    StorageReference st;


    public UserListAdapter(Context contextt, int resource, List<User> objects, Job jobb) {
        super(contextt, resource, objects);
        db = FirebaseDatabase.getInstance().getReference("jobs");
        mObjects = objects;
        layout = resource;
        context = contextt;
        job = jobb;
        st = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mainViewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvUserInfo);
            viewHolder.button = (Button) convertView.findViewById(R.id.btnAcceptRequest);
            viewHolder.image = (CircleImageView) convertView.findViewById(R.id.user_picture);
            viewHolder.button2 = (TextView) convertView.findViewById(R.id.tvViewProfile);
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
                Intent i = new Intent(v.getContext(), JobsActivity.class);
                v.getContext().startActivity(i);
            }
        });

        mainViewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", (User) getItem(position));
                Intent i = new Intent(v.getContext(), ProfileActivity.class);
                i.putExtras(bundle);
                v.getContext().startActivity(i);
            }
        });

        mainViewHolder.title.setText(getItem(position).fullName());
        st.child("users").child(getItem(position).getuID()).child("picture").getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap scaledBmp = bitmap.createScaledBitmap(bitmap, 50, 50, false);
                mainViewHolder.image.setImageBitmap(scaledBmp);
            }
        });

        return convertView;
    }
}
