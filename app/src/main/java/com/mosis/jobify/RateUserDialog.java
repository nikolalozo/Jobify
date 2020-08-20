package com.mosis.jobify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.models.Review;

public class RateUserDialog extends DialogFragment {
    RatingBar ratingBar;
    DatabaseReference db;
    public String jobId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        db = FirebaseDatabase.getInstance().getReference("reviews");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rate_user_dialog, null);
        ratingBar = view.findViewById(R.id.rating_bar);

        builder.setView(view).setTitle("Rate user").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Review review = new Review(ratingBar.getRating(), jobId);
                String ratingId = db.push().getKey();
                db.child(ratingId).setValue(review);
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void setJobId(String id) {
        jobId = id;
    }
}