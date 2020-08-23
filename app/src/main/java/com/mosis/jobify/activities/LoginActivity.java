package com.mosis.jobify.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mosis.jobify.R;
import com.mosis.jobify.Restarter;
import com.mosis.jobify.data.JobsData;
import com.mosis.jobify.data.UsersData;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, passwd;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    Intent mServiceIntent;
    private TrackingService mYourService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.edtTxtEmail);
        passwd = findViewById(R.id.edtTxtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        JobsData.getInstance().init();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = passwd.getText().toString();
                if (email.isEmpty()) {
                    emailId.setError("Please enter email.");
                    emailId.requestFocus();
                }
                if (pwd.isEmpty()) {
                    passwd.setError("Please enter password.");
                    passwd.requestFocus();
                }
                if (!(pwd.isEmpty() && email.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Couldn't login, please try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                if (ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                                {
                                    ActivityCompat.requestPermissions(LoginActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE},2);
                                }
                                else {
                                        startService(new Intent(LoginActivity.this, TrackingService.class));
                                        startActivity(new Intent(LoginActivity.this, MapActivity.class));
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignIn.setEnabled(false);
        //region TextChange Listeners
        emailId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty()){
                    btnSignIn.setEnabled(false);
                }
                else {
                    btnSignIn.setEnabled(true);
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
                if(emailId.getText().toString().isEmpty() || passwd.getText().toString().isEmpty()){
                    btnSignIn.setEnabled(false);
                }
                else {
                    btnSignIn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                finish();
                System.exit(0);
            }
        }
                startService(new Intent(LoginActivity.this, TrackingService.class));
                        Intent i = new Intent(LoginActivity.this, MapActivity.class);
                        startActivity(i);
                        finish();
    }
}
