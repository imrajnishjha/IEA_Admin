package com.example.ieaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class memberApprovalDetail extends AppCompatActivity {

    TextView newMemberName,newMemberEmail,newMemberCompany,newMemberTurnover,newMemberIndustry,newMemberMembership,newMemberAmountLeft,newMemberContact,newMemberPaymentReceiver;
    ImageView newMemberPayProof;
    DatabaseReference databaseReference;
    AppCompatButton approvalbackbtn,approveBtn;
    FirebaseDatabase memberDirectoryRoot;
    DatabaseReference memberDirectoryRef,registrationDataRef,tempRegistrationData;
    StorageReference defaultProfilePicReference,newReference;

    String newName,newEmail,newCompany,newIndustry,newAmountLeft,newphoneno,newProofUrl,newTurnover,newMembership,newpaymentReceiver;

    String joiningDate,nullString;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_approval_detail);


        defaultProfilePicReference = FirebaseStorage.getInstance().getReference();
        joiningDate="14th Nov 2022";
        nullString="";


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
        newMemberPaymentReceiver=findViewById(R.id.new_member_paymentReceiver);

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
                    newpaymentReceiver=snapshot.child("paymentReceiverName").getValue().toString();

                    newMemberName.setText(newName);
                    newMemberEmail.setText(newEmail);
                    newMemberCompany.setText(newCompany);
                    newMemberIndustry.setText(newIndustry);
                    newMemberAmountLeft.setText(newAmountLeft);
                    newMemberContact.setText(newphoneno);
                    newMemberTurnover.setText(newTurnover);
                    newMemberMembership.setText(newMembership);
                    newMemberPaymentReceiver.setText(newpaymentReceiver);

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
                memberDirectoryRef = memberDirectoryRoot.getReference("Registered Users");
                registrationDataRef = memberDirectoryRoot.getReference("Registration Data");
                tempRegistrationData = memberDirectoryRoot.getReference("Temp Registry").child(newEmail.replaceAll("\\.", "%7"));
                RegistrationDataModel approveRegistrationData = new RegistrationDataModel(newMembership, newTurnover, newProofUrl, newEmail,newAmountLeft,newIndustry,newpaymentReceiver);


                StorageReference fileRef = defaultProfilePicReference.child("User Profile Pictures/"+newEmail+"ProfilePicture");
                Bitmap bitmapDefault = BitmapFactory.decodeResource(getResources(),R.drawable.iea_logo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapDefault.compress(Bitmap.CompressFormat.JPEG,100, baos);
                byte[] dataImg = baos.toByteArray();
                fileRef.putBytes(dataImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                memberApprovalDetailModel approveMemberDirectoryDetailModel = new memberApprovalDetailModel(nullString, newCompany, nullString,joiningDate,newEmail,nullString,newName,newphoneno,uri.toString());
                                memberDirectoryRef.child(newEmail.replaceAll("\\.", "%7")).setValue(approveMemberDirectoryDetailModel);

                                registrationDataRef.child(newEmail.replaceAll("\\.", "%7")).setValue(approveRegistrationData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        tempRegistrationData.removeValue();
                                        Toast.makeText(memberApprovalDetail.this, "Member Approved", Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });

                    }
                });
                finish();
            }
        });
    }
}