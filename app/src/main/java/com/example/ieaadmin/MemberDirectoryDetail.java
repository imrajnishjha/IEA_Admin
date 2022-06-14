package com.example.ieaadmin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberDirectoryDetail extends AppCompatActivity {
    ImageView memberProfileImage;
    TextView memberProfileName, memberMembershipId, memberMembershipDate, memberContactNumber, memberDateOfBirth, memberEmailTxtView, memberCompanyName, memberAddress, memberBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_directory_detail);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Registered Users");

        memberProfileImage = findViewById(R.id.member_profile_image);
        memberMembershipId = findViewById(R.id.member_membership_id);
        memberContactNumber = findViewById(R.id.member_contactNumber);
        memberDateOfBirth = findViewById(R.id.member_dateOfBirth);
        memberProfileName = findViewById(R.id.member_profile_name);
        memberMembershipDate = findViewById(R.id.member_membership_date);
        memberEmailTxtView = findViewById(R.id.member_email);
        memberCompanyName = findViewById(R.id.member_company_name);
        memberAddress = findViewById(R.id.member_address);
        memberBio = findViewById(R.id.member_bio);

        String coreItemKey = getIntent().getStringExtra("MemberItemKey");

        ref.child(coreItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String memberMembershipDateStr = snapshot.child("date_of_membership").getValue().toString();
                    String memberMembershipIdStr = snapshot.child("member_id").getValue().toString();
                    String memberPhoneNumberStr = snapshot.child("phone_number").getValue().toString();
                    String memberDOBStr = snapshot.child("date_of_birth").getValue().toString();
                    String memberEmailStr = snapshot.child("email").getValue().toString();
                    String memberCompanyNameStr = snapshot.child("company_name").getValue().toString();
                    String memberAddressStr = snapshot.child("address").getValue().toString();
                    String memberNameStr = snapshot.child("name").getValue().toString();
                    String memberPictureUrl = snapshot.child("purl").getValue().toString();
                    String memberBioStr = snapshot.child("description").getValue().toString();

                    memberMembershipId.setText(memberMembershipIdStr);
                    memberMembershipDate.setText(memberMembershipDateStr);
                    memberContactNumber.setText(memberPhoneNumberStr);
                    memberDateOfBirth.setText(memberDOBStr);
                    memberEmailTxtView.setText(memberEmailStr);
                    memberCompanyName.setText(memberCompanyNameStr);
                    memberAddress.setText(memberAddressStr);
                    memberProfileName.setText(memberNameStr);
                    memberBio.setText(memberBioStr);

                    Glide.with(memberProfileImage.getContext())
                            .load(memberPictureUrl)
                            .placeholder(R.drawable.iea_logo)
                            .circleCrop()
                            .error(R.drawable.iea_logo)
                            .into(memberProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}