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
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;

import java.util.HashMap;
import java.util.Map;

public class RateUserDialog extends DialogFragment {
    RatingBar ratingBar;
    DatabaseReference db;
    public Job job;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        db = FirebaseDatabase.getInstance().getReference("jobs");

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
                DatabaseReference jobRef = db.child(job.getKey());
                Map<String, Object> jobUpdates = new HashMap<>();
                if (job.getIdPosted().equals(UsersData.getInstance().getCurrentUser().getuID())) {
                    job.setReviewByOwner(ratingBar.getRating());
                    jobUpdates.put("reviewByOwner", job.getReviewByOwner());
                } else {
                    job.setReviewByEmployeer(ratingBar.getRating());
                    jobUpdates.put("reviewByEmployeer", job.getReviewByEmployeer());
                }
                jobRef.updateChildren(jobUpdates);
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void setJob(Job jobb) {
        job = jobb;
    }
}