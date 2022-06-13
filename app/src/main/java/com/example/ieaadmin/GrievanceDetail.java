package com.example.ieaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class GrievanceDetail extends AppCompatActivity {



    TextView grievanceEmailTv, grievanceDepartmentTv, grievanceStatusTv, grievanceDescriptionTv;
    AppCompatButton onProgressBtn, solvedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Unsolved Grievances");

        grievanceEmailTv = findViewById(R.id.grievance_email_detail_tv);
        grievanceDepartmentTv = findViewById(R.id.grievance_department_detail_tv);
        grievanceStatusTv = findViewById(R.id.grievance_status_detail_tv);
        grievanceDescriptionTv = findViewById(R.id.grievance_description_detail_tv);
        onProgressBtn = findViewById(R.id.grievance_on_progress_btn);
        solvedBtn = findViewById(R.id.grievance_solved_btn);

        String grievanceItemKey = getIntent().getStringExtra("GrievanceItemKey");

        ref.child(grievanceItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String grievanceEmailStr = snapshot.child("email").getValue().toString();
                    String grievanceDepartmentStr = snapshot.child("department").getValue().toString();
                    String grievanceDescriptionStr = snapshot.child("complain").getValue().toString();
                    String grievanceStatusStr = snapshot.child("status").getValue().toString();

                    grievanceEmailTv.setText("User: "+grievanceEmailStr);
                    grievanceDescriptionTv.setText(grievanceDescriptionStr);
                    grievanceDepartmentTv.setText("Department: "+grievanceDepartmentStr);
                    grievanceStatusTv.setText("Status: "+grievanceStatusStr);

                    if(grievanceStatusStr.equals("On Progress")){
                        onProgressBtn.setBackground(ContextCompat.getDrawable(GrievanceDetail.this, R.drawable.button_style_grey));
                    } else if(grievanceStatusStr.equals("Solved")){
                        solvedBtn.setBackground(ContextCompat.getDrawable(GrievanceDetail.this, R.drawable.button_style_grey));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        onProgressBtn.setOnClickListener(view -> {
            onProgressBtn.setBackground((ContextCompat.getDrawable(GrievanceDetail.this,R.drawable.button_style_grey)));
            solvedBtn.setBackground((ContextCompat.getDrawable(GrievanceDetail.this,R.drawable.button_style_black)));

            HashMap grievanceStatusHash = new HashMap();
            grievanceStatusHash.put("status", "On Progress");

            ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
        });

        solvedBtn.setOnClickListener(view -> {
            solvedBtn.setBackground((ContextCompat.getDrawable(GrievanceDetail.this,R.drawable.button_style_grey)));
            onProgressBtn.setBackground((ContextCompat.getDrawable(GrievanceDetail.this,R.drawable.button_style_black)));

            HashMap grievanceStatusHash = new HashMap();
            grievanceStatusHash.put("status", "Solved");

            ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
        });
    }
}