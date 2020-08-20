package com.mosis.jobify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.models.Review;

public class RateUserDialog extends DialogFragment {
    RatingBar ratingBar;
    Button btnSubmit;
    DatabaseReference db;
    public String jobId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        db = FirebaseDatabase.getInstance().getReference("reviews");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rate_user_dialog, null);
        ratingBar = view.findViewById(R.id.rating_bar);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review review = new Review(ratingBar.getRating(), jobId);
                String ratingId = db.push().getKey();
                db.child(ratingId).setValue(review);
                dismiss();
            }
        });

        return builder.create();
    }

    public void setJobId(String id) {
        jobId = id;
    }
}