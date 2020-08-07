package com.mosis.jobify.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.models.Review;

import java.util.ArrayList;

public class ReviewsData {
    public ArrayList<Review> reviews;
    private DatabaseReference db;

    public ReviewsData() {
        this.reviews = new ArrayList<Review>();
        db = FirebaseDatabase.getInstance().getReference();
        addListeners();
    }

    private void addListeners() {
        db.child("reviews").addChildEventListener((new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String reviewKey = snapshot.getKey();
                Review newReview = snapshot.getValue(Review.class);
                newReview.key = reviewKey;
                reviews.add(newReview);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String reviewKey = snapshot.getKey();
                Review removedReview = snapshot.getValue(Review.class);
                removedReview.key = reviewKey;
                for(int i = 0; i < reviews.size(); i++) {
                    if(removedReview.key.equals(reviews.get(i).key)) {
                        reviews.remove(i);
                        i--;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
    }

}
