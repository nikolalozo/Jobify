package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.R;
import com.mosis.jobify.models.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    EditText emailId, passwd, confirm, firstName, lastName, date;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference db;
    private StorageReference st;
    User user;
    ImageView pic;
    Uri picUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        emailId = findViewById(R.id.edtTxtEmail);
        passwd = findViewById(R.id.edtTxtPassword);
        confirm = findViewById(R.id.etConfirm);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = passwd.getText().toString();
                String conf = confirm.getText().toString();
                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
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
                ArrayList<String> arr = new ArrayList<String>();
                arr.add("UwsBjtcQWZTEnSYl8PMZRT2HvNk1");
                user.connections=arr;

                mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            db.child("users").child(mFirebaseAuth.getCurrentUser().getUid()).setValue(user);
                            Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
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

    private void takePic(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(i, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
        {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            Bitmap squareBmp = cropToSquare(bmp);
            pic.setImageBitmap(squareBmp);

        }
    }

    private void postPic(){
        FirebaseUser currentUser=mFirebaseAuth.getCurrentUser();
        pic.setDrawingCacheEnabled(true);
        pic.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) pic.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        st.child("users").child(currentUser.getUid()).child("profile").putBytes(data);
    }

    public static Bitmap cropToSquare(Bitmap bitmap){
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
