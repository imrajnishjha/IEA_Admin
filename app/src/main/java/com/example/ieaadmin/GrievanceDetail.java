package com.example.ieaadmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GrievanceDetail extends AppCompatActivity {

    TextView grievanceEmailTv, grievanceDepartmentTv, grievanceStatusTv, grievanceDescriptionTv, grievanceSubjectTv;
    AppCompatButton grievanceSetStatusBtn;
    FirebaseDatabase solvedGrievanceRoot;
    DatabaseReference solvedGrievanceRef, rejectedGrievanceRef, ref2;
    String grievanceEmailStr, grievanceDepartmentStr, grievanceDescriptionStr, grievanceStatusStr, grievanceSubjectStr;
    AutoCompleteTextView grievanceStatusField;
    String grievanceItemKey;
    CardView grievanceShareEmailCv, grievanceShareTwitterCv, grievanceShareFbCv, grievanceShareInstagramCv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Unsolved Grievances");

        grievanceEmailTv = findViewById(R.id.grievance_email_detail_tv);
        grievanceDepartmentTv = findViewById(R.id.grievance_department_detail_tv);
        grievanceStatusTv = findViewById(R.id.grievance_status_detail_tv);
        grievanceDescriptionTv = findViewById(R.id.grievance_description_detail_tv);
        grievanceSubjectTv = findViewById(R.id.grievance_subject_detail_tv);
        grievanceStatusField = findViewById(R.id.grievance_status_field);
        grievanceSetStatusBtn = findViewById(R.id.grievance_set_status_btn);
        grievanceShareEmailCv = findViewById(R.id.grievance_share_email_cv);
        grievanceShareTwitterCv = findViewById(R.id.grievance_share_twitter_cv);
        grievanceShareFbCv = findViewById(R.id.grievance_share_fb_cv);
        grievanceShareInstagramCv = findViewById(R.id.grievance_share_instagram_cv);

        dropdownInit();

        grievanceItemKey = getIntent().getStringExtra("GrievanceItemKey");

        ref.child(grievanceItemKey).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    grievanceEmailStr = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    grievanceDepartmentStr = Objects.requireNonNull(snapshot.child("department").getValue()).toString();
                    grievanceDescriptionStr = Objects.requireNonNull(snapshot.child("complain").getValue()).toString();
                    grievanceStatusStr = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                    grievanceSubjectStr = Objects.requireNonNull(snapshot.child("subject").getValue()).toString();

                    Log.d("Email string", grievanceEmailStr);

                    ref2 = FirebaseDatabase.getInstance().getReference().child("Unresolved Grievances").child(grievanceEmailStr.replaceAll("\\.", "%7"));

                    grievanceEmailTv.setText("User: " + grievanceEmailStr);
                    grievanceDescriptionTv.setText(grievanceDescriptionStr);
                    grievanceDepartmentTv.setText("Department: " + grievanceDepartmentStr);
                    grievanceStatusTv.setText("Status: " + grievanceStatusStr);
                    grievanceSubjectTv.setText("Subject: " + grievanceSubjectStr);

                    switch (grievanceStatusStr) {
                        case "On Progress":
                            grievanceStatusField.setHint("On Progress");
                            break;
                        case "Solved":
                            grievanceStatusField.setHint("Solved");
                            break;
                        case "Under Review":
                            grievanceStatusField.setHint("Under Review");
                            break;
                        case "Unsolved":
                            grievanceStatusField.setHint("Unsolved");
                            break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final String[] grievanceStatus = new String[1];

        FirebaseDatabase.getInstance().getReference().child("Unsolved Grievances").child(grievanceItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                grievanceStatus[0] = Objects.requireNonNull(snapshot.child("status").getValue()).toString();
                Log.d("Grievance Status", "Grievance Status = " + grievanceStatus[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        grievanceSetStatusBtn.setOnClickListener(view -> {
            boolean sendNotification = false;

            if (grievanceStatusField.getText().toString().equals("On Progress") && !grievanceStatus[0].equals("On Progress")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "On Progress");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification = true;

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "On progress working");
            } else if (grievanceStatusField.getText().toString().equals("Under Review") && !grievanceStatus[0].equals("Under Review")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Under Review");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification = true;

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "Under Review working");
            } else if (grievanceStatusField.getText().toString().equals("Rejected") && !grievanceStatus[0].equals("Rejected")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Rejected");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification = true;

                DatabaseReference rejectedGrievanceAddRef = FirebaseDatabase.getInstance().getReference("Rejected Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                GrievanceModel solvedModel = new GrievanceModel(grievanceDescriptionStr, grievanceDepartmentStr, grievanceEmailStr, "solved");
                rejectedGrievanceAddRef.setValue(solvedModel);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "Rejected working");

                rejectedGrievanceRef = solvedGrievanceRoot.getReference("Unsolved Grievances").child(grievanceItemKey);
                rejectedGrievanceRef.removeValue();
                finish();

                Log.d("Unresolved", "Unresolved removal" + rejectedGrievanceRef);
            } else if (grievanceStatusField.getText().toString().equals("Solved") && !grievanceStatus[0].equals("Solved")) {
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Solved");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                sendNotification = true;

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                GrievanceModel solvedModel = new GrievanceModel(grievanceDescriptionStr, grievanceDepartmentStr, grievanceEmailStr, "solved");
                solvedGrievanceRef.setValue(solvedModel);
            }

            if (sendNotification) {
                Toast.makeText(this, "Status changed", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Registered Users/" + grievanceEmailStr.replaceAll("\\.", "%7")).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String grievanceUserToken = Objects.requireNonNull(snapshot.child("user_token").getValue()).toString();
                        FcmNotificationsSender grievanceNotificationSender = new FcmNotificationsSender(grievanceUserToken, "IEA Grievance Update", "Your grievance is " + grievanceStatusField.getText().toString(), getApplicationContext(), GrievanceDetail.this);
                        grievanceNotificationSender.SendNotifications();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });

        grievanceShareEmailCv.setOnClickListener(view -> shareGrievanceEmail());

        grievanceShareTwitterCv.setOnClickListener(view -> shareGrievanceTwitter());

        grievanceShareFbCv.setOnClickListener(view -> shareGrievanceFb());

        grievanceShareInstagramCv.setOnClickListener(view -> shareGrievanceInstagram());
    }

    private void shareGrievanceInstagram() {
        Intent shareIntent;
        String shareText = grievanceSubjectStr + "\n\n" + grievanceDescriptionStr;
        if (doesPackageExist("com.instagram.android")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.instagram.android");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else if (doesPackageExist("com.instagram.lite")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.instagram.lite");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else {
            Toast.makeText(this, "Please install Instagram", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareGrievanceFb() {
        Intent shareIntent;
        String shareText = grievanceSubjectStr + "\n\n" + grievanceDescriptionStr;
        if (doesPackageExist("com.facebook.katana")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.facebook.katana");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else if (doesPackageExist("com.facebook.lite")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.facebook.lite");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            startActivity(shareIntent);
        } else {
            Toast.makeText(this, "Please install Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareGrievanceTwitter() {
        Intent shareIntent;
        String shareText = grievanceSubjectStr + "\n" + grievanceDescriptionStr;
        if (doesPackageExist("com.twitter.android")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.twitter.android");
            shareIntent.putExtra(Intent.EXTRA_TITLE, grievanceSubjectStr);
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        } else if (doesPackageExist("com.twitter.android.lite")) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.setPackage("com.twitter.android.lite");
            shareIntent.putExtra(Intent.EXTRA_TITLE, grievanceSubjectStr);
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        }else {
            String tweetUrl = "https://twitter.com/intent/tweet?text=" + grievanceSubjectStr + ": " + grievanceDescriptionStr;
            Uri uri = Uri.parse(tweetUrl);
            shareIntent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(shareIntent);
    }

    private boolean doesPackageExist(String packageName) {
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(packageName))
                return true;
        }
        return false;
    }

    private void shareGrievanceEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, grievanceSubjectStr);
        emailIntent.putExtra(Intent.EXTRA_TEXT, grievanceDescriptionStr);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(GrievanceDetail.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        dropdownInit();
    }

    public void dropdownInit() {
        String[] grievance_departments = getResources().getStringArray(R.array.grievanceStatusArray);
        ArrayAdapter<String> arrayAdapterDepartments = new ArrayAdapter<>(getBaseContext(), R.layout.drop_down_item, grievance_departments);
        AutoCompleteTextView autoCompleteTextViewDepartments = findViewById(R.id.grievance_status_field);
        autoCompleteTextViewDepartments.setAdapter(arrayAdapterDepartments);
    }
}