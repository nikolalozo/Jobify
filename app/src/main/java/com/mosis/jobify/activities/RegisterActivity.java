package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mosis.jobify.R;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.User;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    EditText emailId, passwd, confirm, firstName, lastName, etAge;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference db;
    private StorageReference st;
    User user;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        st= FirebaseStorage.getInstance().getReference();
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        emailId = findViewById(R.id.edtTxtEmail);
        passwd = findViewById(R.id.edtTxtPassword);
        confirm = findViewById(R.id.etConfirm);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);
        imageView=(CircleImageView) findViewById(R.id.profile_image);
        btnSignUp.setEnabled(false);
        etAge=findViewById(R.id.etAge);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, 1);
                }
            }
        });

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etAge.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                    btnSignUp.setEnabled(false);
                }
                else {
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etAge.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                    btnSignUp.setEnabled(false);
                }
                else {
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etAge.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                    btnSignUp.setEnabled(false);
                }
                else {
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etAge.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                    btnSignUp.setEnabled(false);
                }
                else {
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etAge.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                    btnSignUp.setEnabled(false);
                }
                else {
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etAge.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty() || emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty() || confirm.getText().toString().isEmpty()){
                    btnSignUp.setEnabled(false);
                }
                else {
                    btnSignUp.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = passwd.getText().toString();
                String conf = confirm.getText().toString();
                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
                String age = etAge.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email.");
                    return;
                } else if (pwd.isEmpty()) {
                    passwd.setError("Please enter password.");
                    return;
                } else if (conf.isEmpty()) {
                    confirm.setError("Please enter confirm password");
                    return;
                } else if (first.isEmpty()) {
                    firstName.setError("Please enter first name");
                    return;
                } else if (last.isEmpty()) {
                    lastName.setError("Please enter last name");
                    return;
                } else if(age.isEmpty()) {
                    etAge.setError("Please enter age.");
                    return;
                }

                user = new User();
                user.email = email;
                if (pwd.equals(conf)) {
                    user.password = pwd;
                } else {
                    confirm.setError("Not same as password.");
                    passwd.setError("Not same as confirm password");
                    return;
                }
                user.firstName=first;
                user.lastName=last;
                user.years=Integer.parseInt(age);

                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.child("users").child(mFirebaseAuth.getCurrentUser().getUid()).setValue(user);
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            imageView.setDrawingCacheEnabled(true);
                            imageView.buildDrawingCache();
                            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] data = baos.toByteArray();
                            st.child("users").child(mFirebaseAuth.getCurrentUser().getUid()).child("picture").putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (ActivityCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                                    {
                                        ActivityCompat.requestPermissions(RegisterActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE},2);
                                    }
                                    else {
                                        startService(new Intent(RegisterActivity.this, TrackingService.class));
                                                Intent i = new Intent(RegisterActivity.this, MapActivity.class);
                                                startActivity(i);
                                                finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull final int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                finish();
                System.exit(0);
            }
        }
                startService(new Intent(RegisterActivity.this, TrackingService.class));
                        Intent i = new Intent(RegisterActivity.this, MapActivity.class);
                        startActivity(i);
                        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap squarePhoto = crop(photo);
            imageView.setImageBitmap(squarePhoto);
        }
    }

    public static Bitmap crop(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
    }
}
