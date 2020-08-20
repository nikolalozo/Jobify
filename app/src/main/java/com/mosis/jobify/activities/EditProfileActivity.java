package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mosis.jobify.ChangePasswordDialog;
import com.mosis.jobify.R;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    User currentUser;
    EditText etFirstName, etSurname, etEmail, etYears, etProfession;
//    TextView tvChangePassword;
    Button btnEdit;
    DatabaseReference db;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        currentUser = UsersData.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseAuth = FirebaseAuth.getInstance();

        ImageView imageView = findViewById(R.id.editProfileImage);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ema);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);

        etFirstName = findViewById(R.id.etUserFirstName);
        etSurname = findViewById(R.id.etUserSurname);
        etEmail = findViewById(R.id.etUserEmail);
        etYears = findViewById(R.id.etUserYears);
        etProfession = findViewById(R.id.etUserProfession);
//        tvChangePassword = findViewById(R.id.tvChangePassw);
        etFirstName.setText(currentUser.getFirstName());
        etSurname.setText(currentUser.getLastName());
        etEmail.setText(currentUser.getEmail());
        btnEdit = findViewById(R.id.btnEdit);
        if (currentUser.getYears() > 0) {
            etYears.setText(String.valueOf(currentUser.getYears()));
        }
        if (currentUser.getProfession() != null) {
            etProfession.setText(currentUser.getProfession());
        }

//        tvChangePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog();
//                changePasswordDialog.show(getSupportFragmentManager(), "change password dialog");
//            }
//        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etSurname.getText().toString();
                String email = etEmail.getText().toString();
                String userYears = etYears.getText().toString();
                int years = Integer.parseInt(userYears);
                String profession = etProfession.getText().toString();
                if (firstName.isEmpty()) {
                    etFirstName.setError("Please enter first name.");
                    return;
                } else if (lastName.isEmpty()) {
                    etSurname.setError("Please enter surname.");
                    return;
                } else if (email.isEmpty()) {
                    etEmail.setError("Please enter email.");
                    return;
                }
                currentUser.setFirstName(firstName);
                currentUser.setLastName(lastName);
                currentUser.setEmail(email);
                currentUser.setYears(years);
                currentUser.setProfession(profession);
                mFirebaseAuth.getCurrentUser().updateEmail(email);

                DatabaseReference userRef = db.child(currentUser.getuID());
                userRef.setValue(currentUser);

                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

    }
}
