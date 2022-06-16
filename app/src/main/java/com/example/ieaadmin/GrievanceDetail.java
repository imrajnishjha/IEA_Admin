package com.example.ieaadmin;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.UUID;

public class GrievanceDetail extends AppCompatActivity {



    TextView grievanceEmailTv, grievanceDepartmentTv, grievanceStatusTv, grievanceDescriptionTv;
    AppCompatButton onProgressBtn, solvedBtn, grievanceSetStatusBtn;
    FirebaseDatabase solvedGrievanceRoot;
    DatabaseReference solvedGrievanceRef, rejectedGrievanceRef, ref2;
    FirebaseAuth mAuth;
    String grievanceEmailStr,grievanceDepartmentStr,grievanceDescriptionStr;
    AutoCompleteTextView grievanceStatusField;
    String grievanceItemKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grievance_detail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Unsolved Grievances");



        grievanceEmailTv = findViewById(R.id.grievance_email_detail_tv);
        grievanceDepartmentTv = findViewById(R.id.grievance_department_detail_tv);
        grievanceStatusTv = findViewById(R.id.grievance_status_detail_tv);
        grievanceDescriptionTv = findViewById(R.id.grievance_description_detail_tv);
        grievanceStatusField = findViewById(R.id.grievance_status_field);
        grievanceSetStatusBtn = findViewById(R.id.grievance_set_status_btn);

        dropdownInit();

        grievanceItemKey = getIntent().getStringExtra("GrievanceItemKey");

        ref.child(grievanceItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    grievanceEmailStr = snapshot.child("email").getValue().toString();
                    grievanceDepartmentStr = snapshot.child("department").getValue().toString();
                    grievanceDescriptionStr = snapshot.child("complain").getValue().toString();
                    String grievanceStatusStr = snapshot.child("status").getValue().toString();

                    Log.d("Email string", grievanceEmailStr);

                     ref2 = FirebaseDatabase.getInstance().getReference().child("Unresolved Grievances").child(grievanceEmailStr.replaceAll("\\.", "%7"));

                    grievanceEmailTv.setText("User: "+grievanceEmailStr);
                    grievanceDescriptionTv.setText(grievanceDescriptionStr);
                    grievanceDepartmentTv.setText("Department: "+grievanceDepartmentStr);
                    grievanceStatusTv.setText("Status: "+grievanceStatusStr);

                    if(grievanceStatusStr.equals("On Progress")){
                        grievanceStatusField.setHint("On Progress");
                    } else if(grievanceStatusStr.equals("Solved")){
                        grievanceStatusField.setHint("Solved");
                    }else if(grievanceStatusStr.equals("Under Review")){
                        grievanceStatusField.setHint("Under Review");
                    }else if(grievanceStatusStr.equals("Unsolved")){
                        grievanceStatusField.setHint("Unsolved");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        grievanceSetStatusBtn.setOnClickListener(view -> {
            if(grievanceStatusField.getText().toString().equals("On Progress")){
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "On Progress");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "On progress working");
            } else if(grievanceStatusField.getText().toString().equals("Under Review")){
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Under Review");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "Under Review working");
            }else if(grievanceStatusField.getText().toString().equals("Rejected")){
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Rejected");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                DatabaseReference rejectedGrievanceAddRef = FirebaseDatabase.getInstance().getReference("Rejected Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                GrievanceModel solvedmodel=new GrievanceModel(grievanceDescriptionStr,grievanceDepartmentStr,grievanceEmailStr,"solved");
                rejectedGrievanceAddRef.setValue(solvedmodel);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                solvedGrievanceRef.removeValue();
                Log.d("On progress", "Rejected working");

                rejectedGrievanceRef = solvedGrievanceRoot.getReference("Unsolved Grievances").child(grievanceItemKey);
                rejectedGrievanceRef.removeValue();


                Log.d("Unresolved", "Unresolved removal"+rejectedGrievanceRef);
            }else if(grievanceStatusField.getText().toString().equals("Solved")){
                HashMap grievanceStatusHash = new HashMap();
                grievanceStatusHash.put("status", "Solved");

                ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
                ref2.child(grievanceItemKey).updateChildren(grievanceStatusHash);

                solvedGrievanceRoot = FirebaseDatabase.getInstance();
                solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
                        .child(grievanceItemKey);
                GrievanceModel solvedmodel=new GrievanceModel(grievanceDescriptionStr,grievanceDepartmentStr,grievanceEmailStr,"solved");
                solvedGrievanceRef.setValue(solvedmodel);
            }
            Toast.makeText(this, "Status changed", Toast.LENGTH_SHORT).show();
        });



//        solvedBtn.setOnClickListener(view -> {
//            solvedBtn.setBackground((ContextCompat.getDrawable(GrievanceDetail.this,R.drawable.button_style_grey)));
//            onProgressBtn.setBackground((ContextCompat.getDrawable(GrievanceDetail.this,R.drawable.button_style_black)));
//
//            HashMap grievanceStatusHash = new HashMap();
//            grievanceStatusHash.put("status", "Solved");
//
//            ref.child(grievanceItemKey).updateChildren(grievanceStatusHash);
//
//
//            solvedGrievanceRoot = FirebaseDatabase.getInstance();
//            solvedGrievanceRef = solvedGrievanceRoot.getReference("Solved Grievance").child(grievanceEmailStr.replaceAll("\\.", "%7"))
//                    .child(UUID.randomUUID().toString());
//            GrievanceModel solvedmodel=new GrievanceModel(grievanceDescriptionStr,grievanceDepartmentStr,grievanceEmailStr,"solved");
//            solvedGrievanceRef.setValue(solvedmodel);
//
//        });
    }

    @Override
    public void onResume(){
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