package com.mosis.jobify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mosis.jobify.activities.MapActivity;
import com.mosis.jobify.models.Job;

public class ConfirmJobDialog extends DialogFragment {
    public Job job;
    String message;
    public boolean cancelJ = false;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.message).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cancelJ) {
                    Intent i = new Intent(getContext(), MapActivity.class);
                    startActivity(i);
                }
            }
        });
        if (cancelJ) {
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        return builder.create();
    }

    public void setValue(Job j) {
        this.job = j;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void cancelJob() {
        cancelJ = true;
    }
}
