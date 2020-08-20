package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    ListView lvSearch;
    SearchView searchView;
    public ArrayAdapter<User> adapterUsers;
    public ArrayAdapter<Job> adapterJobs;
    boolean searchForConnections;
    RadioGroup radioGroup;
    RadioButton rb1, rb2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchForConnections=true;
        lvSearch=findViewById(R.id.lvSearch);
        searchView=findViewById(R.id.searchView);
        radioGroup=findViewById(R.id.radioGroup);
        rb1=findViewById(R.id.radio_button_1);
        rb2=findViewById(R.id.radio_button_2);
        adapterUsers= new ArrayAdapter<User>(this, R.layout.list_item, UsersData.getInstance().users);
        adapterJobs=new ArrayAdapter<Job>(this, R.layout.list_item, JobsData.getInstance().getJobs());
        lvSearch.setAdapter(adapterUsers);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query)
                    {
                        if(searchForConnections)
                        adapterUsers.getFilter().filter(query);
                        else
                            adapterJobs.getFilter().filter(query);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText)
                    {
                        if(searchForConnections)
                        adapterUsers.getFilter().filter(newText);
                        else
                            adapterJobs.getFilter().filter(newText);
                        return false;
                    }
                });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_1:
                        searchForConnections = true;
                        lvSearch.setAdapter(adapterUsers);
                        break;
                    case R.id.radio_button_2:
                        searchForConnections = false;
                        lvSearch.setAdapter(adapterJobs);
                        break;
                }
            }
        });

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MapActivity.search=true;
                if(searchForConnections) {
                    MapActivity.newLat= adapterUsers.getItem(position).lat;
                    MapActivity.newLng=adapterUsers.getItem(position).lng;
                }
                else {
                    MapActivity.newLat= adapterJobs.getItem(position).latitude;
                    MapActivity.newLng=adapterJobs.getItem(position).longitude;
                }
                finish();
            }
        });
    }
}

