package com.mosis.jobify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mosis.jobify.models.User;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {
    private int layout;
    public Context context;
    private List<User> mObjects;

    public UserListAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        mObjects = objects;
        layout = resource;
        context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        UserViewHolder mainViewHolder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);
            UserViewHolder viewHolder = new UserViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvUserInfo);
            viewHolder.acceptButton = (Button) convertView.findViewById(R.id.btnAcceptRequest);
            viewHolder.declineButton = (Button) convertView.findViewById(R.id.btnDeclineRequest);
            convertView.setTag(viewHolder);
        }
        mainViewHolder = (UserViewHolder) convertView.getTag();
        mainViewHolder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(v.getContext(),
                        "This is a message displayed in a Toast",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        });

        mainViewHolder.declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(v.getContext(),
                        "Ema",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        });

        mainViewHolder.title.setText(getItem(position).fullName());

        return convertView;
    }
}
