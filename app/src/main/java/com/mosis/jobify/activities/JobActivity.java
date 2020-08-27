package com.mosis.jobify.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mosis.jobify.R;
import com.mosis.jobify.StatusEnum;
import com.mosis.jobify.data.UsersData;
import com.mosis.jobify.models.Job;
import com.mosis.jobify.models.User;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobActivity extends AppCompatActivity {
    Job job;
    private DatabaseReference db;
    TextView tvJobTitle, tvPay, tvDate, tvDescription, tvPostedDate, tvApplyBy, tvHour, tvPerson, tvAlreadyApplied;
    Button btnViewProfile, btnApply;
    StorageReference st;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);
        db = FirebaseDatabase.getInstance().getReference("jobs");
        st = FirebaseStorage.getInstance().getReference();
        tvJobTitle = findViewById(R.id.tvTitle);
        tvPay = findViewById(R.id.pay);
        tvDate = findViewById(R.id.tvDate);
        tvDescription = findViewById(R.id.tvDescription);
        tvPostedDate = findViewById(R.id.publishedDate);
        tvApplyBy = findViewById(R.id.applyByDate);
        tvHour = findViewById(R.id.tvHour);
        tvPerson = findViewById(R.id.tvOwner);
        btnViewProfile = findViewById(R.id.btnViewProfile);
        btnApply = findViewById(R.id.btnApply);
        tvAlreadyApplied = findViewById(R.id.tvAlreadyApplied);
        toolbar = findViewById(R.id.toolbarJob);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ImageView imageView = (CircleImageView) findViewById(R.id.user_picture);

        tvHour.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_hourglass, 0, 0, 0);
        Bundle extras = getIntent().getExtras();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.getDefault());

        if (extras != null) {
            job = (Job) extras.get("job");
            tvJobTitle.setText(job.getTitle());
            tvPay.setText(job.getWage() + " RSD");
            tvDate.setText("Date: " + dateFormat.format(job.getDate()));
            tvDescription.setText(job.getDescription());
            tvPostedDate.setText(dateFormat.format(job.getDatePosted()));
            tvApplyBy.setText(dateFormat.format(job.getAppliedBy()));
            tvHour.setText(timeFormat.format(job.getDate()));
            tvPerson.setText(UsersData.getInstance().getUser(job.getIdPosted()).fullName());
            btnViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", UsersData.getInstance().getUser(job.getIdPosted()));
                    Intent i = new Intent(JobActivity.this, ProfileActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });
            String currUserId = UsersData.getInstance().getCurrentUser().getuID();
            if (job.status == StatusEnum.DONE) {
                btnApply.setVisibility(View.INVISIBLE);
                tvAlreadyApplied.setVisibility(View.VISIBLE);
                tvAlreadyApplied.setText("Finished job");
                return;
            } else if (job.getAppliedBy().compareTo(new Date()) > 0) {
                btnApply.setVisibility(View.INVISIBLE);
                tvAlreadyApplied.setText("Date for applying has passed");
                return;
            } else if (job.getIdPosted().equals(currUserId)) {
                btnApply.setVisibility(View.INVISIBLE);
                tvAlreadyApplied.setVisibility(View.INVISIBLE);
            } else if (job.arrayIdRequested.contains(currUserId)) {
                btnApply.setVisibility(View.INVISIBLE);
            } else {
                tvAlreadyApplied.setVisibility(View.INVISIBLE);
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        job.addRequest(UsersData.getInstance().getCurrentUser().getuID());
                        DatabaseReference jobRef = db.child(job.getKey());
                        Map<String, Object> jobUpdates = new HashMap<>();
                        jobUpdates.put("arrayIdRequested", job.arrayIdRequested);
                        jobRef.updateChildren(jobUpdates);
                        btnApply.setVisibility(View.INVISIBLE);
                        tvAlreadyApplied.setVisibility(View.VISIBLE);
                    }
                });
            }

            st.child("users").child(job.getIdPosted()).child("picture").getBytes(5 * 1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap scaledBmp = bitmap.createScaledBitmap(bitmap, imageView.getWidth(), imageView.getHeight(), false);
                    imageView.setImageBitmap(scaledBmp);
                }
            });
        }


    }
}
