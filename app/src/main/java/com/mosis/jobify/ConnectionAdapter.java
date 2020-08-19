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
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.mosis.jobify.models.User;

import java.util.ArrayList;

public class ConnectionAdapter extends ArrayAdapter<User> {

    Context context;
    ArrayList<User> users;

    public ConnectionAdapter(Context c, ArrayList<User> usersArray) {
        super(c, R.layout.row_connection, usersArray);
        this.context = c;
        this.users = usersArray;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_connection, parent, false);
        ImageView images = row.findViewById(R.id.connectionImage);
        TextView myTitle = row.findViewById(R.id.connectionFullName);

        myTitle.setText(getItem(position).fullName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ema);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        images.setImageDrawable(roundedBitmapDrawable);

        return row;
    }
}
