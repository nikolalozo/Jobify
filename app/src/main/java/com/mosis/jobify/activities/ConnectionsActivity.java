package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mosis.jobify.ConnectionAdapter;
import com.mosis.jobify.CurrentJobListAdapter;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ConnectionsActivity extends AppCompatActivity {
    public ArrayList<User> connections;
    ListView lvConnections;
    TextView tvNoConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);
        tvNoConnections = findViewById(R.id.tvNoConnections);

        connections = UsersData.getInstance().getUserConnections();
        lvConnections = findViewById((R.id.lvConnections));
        lvConnections.setAdapter(new ConnectionAdapter(this, connections));
        if (connections.size() > 0) {
            tvNoConnections.setVisibility(View.INVISIBLE);
        }


        lvConnections.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ConnectionsActivity.this, "Facebook Description", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
