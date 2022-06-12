package com.example.ieaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class memberApprovalDetail extends AppCompatActivity {

    TextView newMemberName,newMemberEmail,newMemberCompany,newMemberTurnover,newMemberIndustry,newMemberMembership,newMemberAmountLeft,newMemberContact;
    ImageView newMemberPayProof;
    DatabaseReference databaseReference;
    AppCompatButton approvalbackbtn,approveBtn;
    FirebaseDatabase memberDirectoryRoot;
    DatabaseReference memberDirectoryRef,registrationDataRef,tempRegistrationData;

    String newName,newEmail,newCompany,newIndustry,newAmountLeft,newphoneno,newProofUrl,newTurnover,newMembership;

    String Purl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_approval_detail);

        Purl="https://firebasestorage.googleapis.com/v0/b/iea-app.appspot.com/o/default_profile_picture.jpg?alt=media&token=af41ca91-9929-46b5-b9a3-e8ff6c258495";

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Temp Registry");
        newMemberName = findViewById(R.id.new_member_name);
        newMemberEmail=findViewById(R.id.new_member_email);
        newMemberCompany=findViewById(R.id.new_member_company);
        newMemberTurnover=findViewById(R.id.new_member_turnover);
        newMemberIndustry=findViewById(R.id.new_member_industrytype);
        newMemberMembership=findViewById(R.id.new_member_membershiptype);
        newMemberAmountLeft=findViewById(R.id.new_member_payinglater);
        newMemberContact=findViewById(R.id.new_member_phoneno);
        newMemberPayProof=findViewById(R.id.new_member_proof_img);

        approvalbackbtn=findViewById(R.id.approvalDetail_back_button);
        approveBtn=findViewById(R.id.approval_btn);

        approvalbackbtn.setOnClickListener(view -> finish());

        String memberApprovalKey = getIntent().getStringExtra("memberApprovalKey");

        databaseReference.child(memberApprovalKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    newName = snapshot.child("fullname").getValue().toString();
                    newEmail = snapshot.child("email").getValue().toString();
                    newCompany = snapshot.child("companyName").getValue().toString();
                    newTurnover = snapshot.child("turnover").getValue().toString();
                    newIndustry = snapshot.child("department").getValue().toString();
                    newMembership = snapshot.child("memberfee").getValue().toString();
                    newAmountLeft = snapshot.child("amountLeft").getValue().toString();
                    newphoneno  = snapshot.child("phoneNo").getValue().toString();
                    newProofUrl = snapshot.child("imageUrl").getValue().toString();

                    newMemberName.setText(newName);
                    newMemberEmail.setText(newEmail);
                    newMemberCompany.setText(newCompany);
                    newMemberIndustry.setText(newIndustry);
                    newMemberAmountLeft.setText(newAmountLeft);
                    newMemberContact.setText(newphoneno);
                    newMemberTurnover.setText(newTurnover);
                    newMemberMembership.setText(newMembership);

                    Glide.with(newMemberPayProof.getContext())
                            .load(newProofUrl)
                            .error(R.drawable.iea_logo)
                            .into(newMemberPayProof);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        approveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberDirectoryRoot = FirebaseDatabase.getInstance();
                memberDirectoryRef = memberDirectoryRoot.getReference("Members Directory");
                registrationDataRef = memberDirectoryRoot.getReference("Registration Data");
                tempRegistrationData = memberDirectoryRoot.getReference("Temp Registry").child(newEmail.replaceAll("\\.", "%7"));
                memberApprovalDetailModel approveMemberDirectoryDetailModel = new memberApprovalDetailModel(newCompany, newIndustry, newEmail, newName, Purl, newphoneno);
                memberDirectoryRef.child(newEmail.replaceAll("\\.", "%7")).setValue(approveMemberDirectoryDetailModel);
                RegistrationDataModel approveRegistrationData = new RegistrationDataModel(newMembership, newTurnover, newProofUrl, newEmail);
                registrationDataRef.child(newEmail.replaceAll("\\.", "%7")).setValue(approveRegistrationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        tempRegistrationData.removeValue();
                    }
                });
                Toast.makeText(memberApprovalDetail.this, "Member Approved", Toast.LENGTH_LONG).show();


            }
        });

    }
}