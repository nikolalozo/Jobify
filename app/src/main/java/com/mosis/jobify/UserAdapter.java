package com.mosis.jobify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends ArrayAdapter<User> {
    StorageReference st;
    Context context;
    ArrayList<User> users;
    int isRankList = 0;

    public UserAdapter(Context c, ArrayList<User> usersArray) {
        super(c, R.layout.row_user, usersArray);
        this.context = c;
        this.users = usersArray;
        st = FirebaseStorage.getInstance().getReference();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_user, parent, false);
        final ImageView image = (CircleImageView) row.findViewById(R.id.user_picture);
        TextView myTitle = row.findViewById(R.id.connectionFullName);
        TextView rankInfo = row.findViewById(R.id.rankInfo);
        if (isRankList == 0) {
            rankInfo.setVisibility(View.INVISIBLE);
        } else if (isRankList ==1 ) {
            rankInfo.setText("Posted jobs: " + getItem(position).getJobsPosted());
        } else {
            rankInfo.setText("Done jobs: " + getItem(position).getJobsDone());
        }

        myTitle.setText(getItem(position).fullName());
        st.child("users").child(getItem(position).getuID()).child("picture").getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Bitmap scaledBmp = bitmap.createScaledBitmap(bitmap, 50, 50, false);
                image.setImageBitmap(scaledBmp);
            }
        });

        return row;
    }

    public void setIsRankList(int number) {
        this.isRankList = number;
    }
}
