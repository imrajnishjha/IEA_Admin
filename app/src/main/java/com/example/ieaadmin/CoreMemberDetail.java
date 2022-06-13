package com.example.ieaadmin;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CoreMemberDetail extends AppCompatActivity {

    TextView coreTeamMemberDetailDesignation, coreTeamMemberDetailName, coreMemberDetailDepartment;
    ImageView coreTeamMemberDetailProfile;
    AppCompatButton coreTeamMemberContactButton, coreTeamMemberDetailBackButton;
    DatabaseReference databaseReference;
    Dialog coreMemberContactDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_member_detail);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Core Teams Member");

        coreTeamMemberDetailDesignation = findViewById(R.id.core_team_member_designation_text);
        coreTeamMemberDetailName = findViewById(R.id.core_detail_name);
        coreTeamMemberDetailProfile = findViewById(R.id.core_detail_profile_picture);
        coreTeamMemberContactButton = findViewById(R.id.core_detail_contact_button);
        coreTeamMemberDetailBackButton = findViewById(R.id.core_member_detail_back_button);
        coreMemberDetailDepartment = findViewById(R.id.core_detail_department);

        coreMemberContactDialog = new Dialog(this);

        String coreItemKey = getIntent().getStringExtra("CoreItemKey");

        databaseReference.child(coreItemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String coreMemberName = snapshot.child("name").getValue().toString();
                    String coreMemberDesignation = snapshot.child("designation").getValue().toString();
                    String corePictureUrl = snapshot.child("purl").getValue().toString();

                    coreTeamMemberDetailName.setText(coreMemberName);
                    coreTeamMemberDetailDesignation.setText(coreMemberDesignation);

                    Glide.with(coreTeamMemberDetailProfile.getContext())
                            .load(corePictureUrl)
                            .placeholder(R.drawable.iea_logo)
                            .circleCrop()
                            .error(R.drawable.iea_logo)
                            .into(coreTeamMemberDetailProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        coreTeamMemberDetailBackButton.setOnClickListener(view -> finish());

        coreTeamMemberContactButton.setOnClickListener(view -> {

            databaseReference.child(coreItemKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        LayoutInflater inflater = getLayoutInflater();
                        View view = inflater.inflate(R.layout.core_member_contact_popup, null);

                        TextView coreMemberPhoneNumber = view.findViewById(R.id.core_member_phone_number);
                        TextView coreMemberEmail = view.findViewById(R.id.core_member_email);

                        String coreEmail = snapshot.child("mail").getValue().toString();
                        String corePhoneNumber = snapshot.child("phone_no").getValue().toString();

                        coreMemberPhoneNumber.setText(corePhoneNumber);
                        coreMemberEmail.setText(coreEmail);

                        coreMemberContactDialog.setContentView(view);
                        coreMemberContactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        coreMemberContactDialog.show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }
}