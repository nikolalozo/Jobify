package com.mosis.jobify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomInfoDialog extends AppCompatDialogFragment {
    public String message;
    public String title;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        if (title != null) {
            builder.setTitle(title);
        }
        return builder.create();
    }

    public void setMessage(String mess) {
        message = mess;
    }

    public void setTitle(String tit) {
        title = tit;
    }
}
