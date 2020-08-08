package com.mosis.jobify.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.models.User;
import com.mosis.jobify.models.Location;

import java.util.ArrayList;
import java.util.HashMap;

public class UsersData {
    public ArrayList<User> users;
    public ArrayList<User> userConnections = new ArrayList<User>();
    public HashMap<String, Location> userLocations = new HashMap<String, Location>();
    public HashMap<String,Bitmap> connectionPics = new HashMap<>();
    public User currentUser = new User();
    private DatabaseReference db;
    private FirebaseAuth mFirebaseAuth;
    private StorageReference st;

    public UsersData() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        this.users = new ArrayList<User>();
        db = FirebaseDatabase.getInstance().getReference();
        st = FirebaseStorage.getInstance().getReference();
        db.child("users").addListenerForSingleValueEvent(parentEventListener);
        db.child("users").addChildEventListener(childEventListener);
    }

    ValueEventListener parentEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

        ChildEventListener childEventListener=new ChildEventListener() { ///////// PROBAJ DA LI UZIMA IZ BAZE KAD NEMA KONEKCIJE
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String id = dataSnapshot.getKey();
            User user = dataSnapshot.getValue(User.class);
            user.uID = id;
            if(id.equals(mFirebaseAuth.getCurrentUser().getUid())){
                currentUser = user;
            }
            users.add(0,user);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String id = dataSnapshot.getKey();
            User user = dataSnapshot.getValue(User.class);
            user.uID = id;
            for(int i=0;i<users.size();i++){
                if(users.get(i).uID.equals(id)) {
                    users.set(i,user);
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ArrayList<User> getUserConnections() {
        ArrayList<User> arr = new ArrayList<User>();
        for (int i = 0; i < users.size(); i++) {
            if (currentUser.connections.contains(users.get(i).uID)) {
                arr.add(users.get(i));
            }
        }

        userConnections = arr;
        for (int i = 0; i < userConnections.size(); i++) {
            final String id = userConnections.get(i).uID;
            st.child("users").child(id).child("profile").getBytes(5 * 1024 * 1024).addOnCompleteListener(new OnCompleteListener<byte[]>() {
                @Override
                public void onComplete(@NonNull Task<byte[]> task) {
                    byte[] data = task.getResult();
                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Bitmap scaledBmp = bmp.createScaledBitmap(bmp, 100, 100, false);
                    connectionPics.put(id, scaledBmp);
                }
            });
        }
        return arr;
    }

    public User getConnection(String friendId) {
        for(int i=0;i<userConnections.size();i++){
            if(userConnections.get(i).uID.equals(friendId))
                return userConnections.get(i);
        }
        return null;
    }

    private  static class SingletonHolder{
        private static final UsersData instance = new UsersData();
    }

    public static UsersData getInstance(){
        return  UsersData.SingletonHolder.instance;
    }


}
