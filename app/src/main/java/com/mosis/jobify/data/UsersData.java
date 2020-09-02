package com.mosis.jobify.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.BuildConfig;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;
import com.mosis.jobify.models.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class UsersData {
    public ArrayList<User> users, usersPosted;
    public ArrayList<User> userConnections = new ArrayList<User>();
    public HashMap<String,Bitmap> connectionPics = new HashMap<>();
    public User currentUser = new User();
    private DatabaseReference db;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference st;

    public UsersData() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        st= FirebaseStorage.getInstance().getReference();
        users = new ArrayList<User>();
        usersPosted = new ArrayList<User>();
        db = FirebaseDatabase.getInstance().getReference().child("users");
        Query q = db.orderByChild("jobsDone");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    user.uID=snap.getKey();
                    users.add(0, user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query q1 = db.orderByChild("jobsPosted");
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersPosted.clear();
                for(DataSnapshot snap : snapshot.getChildren()) {
                    User user1 = snap.getValue(User.class);
                    user1.setuID(snap.getKey());
                    usersPosted.add(0, user1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public User getCurrentUser() {
        for(int i=0; i<users.size();i++) {
            if(users.get(i).uID.equals(mFirebaseAuth.getCurrentUser().getUid())) {
                currentUser = users.get(i);
            }
        }
        return currentUser;
    }

    public User getUser(String uid) {
        User user = new User();
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getuID().equals(uid)) {
                user = users.get(i);
            }
        }
        return user;
    }

    public ArrayList<User> getUserConnections() {
        ArrayList<User> arr = new ArrayList<User>();
        for (int i = 0; i < users.size(); i++) {
            if (getCurrentUser().connections.contains(users.get(i).uID)) {
                arr.add(users.get(i));
            }
        }
        return arr;
    }

    private  static class SingletonHolder{
        private static final UsersData instance = new UsersData();
    }

    public static UsersData getInstance(){
        return  UsersData.SingletonHolder.instance;
    }


}
