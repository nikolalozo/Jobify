package com.mosis.jobify;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordDialog extends DialogFragment {
    EditText etNewPassword;
    DatabaseReference db;
    FirebaseAuth mFirebaseAuth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        db = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseAuth = FirebaseAuth.getInstance();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_change_password_dialog, null);
        etNewPassword = view.findViewById(R.id.etNewPassword);

        builder.setView(view).setTitle("Enter your old and new password.").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("Change password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPassword = etNewPassword.getText().toString();
                if (newPassword.isEmpty()) {
                    etNewPassword.setError("Please enter new password.");
                    Toast.makeText(getContext(), "Password was not changes because it is empty", Toast. LENGTH_SHORT).show();
                } else {
                    mFirebaseAuth.getCurrentUser().updatePassword(newPassword);
                    User currentUser = UsersData.getInstance().getCurrentUser();
                    DatabaseReference userRef = db.child(currentUser.getuID());
                    Map<String, Object> userUpdates = new HashMap<>();
                    userUpdates.put("password", currentUser.getPassword());

                    userRef.updateChildren(userUpdates);
                    dialog.dismiss();
                }
            }
        });

        return builder.create();
    }
}
