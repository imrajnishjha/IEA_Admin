package com.example.ieaadmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class explore_menu extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView exploreUsername, descriptionUsername;
    ImageView logoutImg, userImage;
    CardView coreMembersCard, memberDirectoryCard, grievanceCard, contactUs, newMembers,bbas;
    Dialog exploreIeaContactDialog;
    DatabaseReference databaseReference;
    StorageReference storageProfilePicReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_menu);

        exploreUsername = findViewById(R.id.explore_username);
        descriptionUsername = findViewById(R.id.description_username);
        logoutImg = findViewById(R.id.logout_img);
        coreMembersCard = findViewById(R.id.core_mem);
        memberDirectoryCard = findViewById(R.id.member_directory);
        grievanceCard = findViewById(R.id.grievance);
        contactUs = findViewById(R.id.explore_menu_contact_us_cardView);
        newMembers = findViewById(R.id.new_member);
        bbas = findViewById(R.id.bbas);
        exploreIeaContactDialog = new Dialog(this);
        userImage = findViewById(R.id.user_img);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registered Users");
        storageProfilePicReference = FirebaseStorage.getInstance().getReference();

        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        assert userEmail != null;
        String userEmailConverted = userEmail.replaceAll("\\.", "%7");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Registered Users/" + userEmailConverted);

        StorageReference fileRef = storageProfilePicReference.child("User Profile Pictures/" + mAuth.getCurrentUser().getEmail().toString()+"ProfilePicture");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(userImage.getContext())
                        .load(uri)
                        .placeholder(R.drawable.iea_logo)
                        .circleCrop()
                        .error(R.drawable.iea_logo)
                        .into(userImage);
            }
        });

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userNameDatabase = Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString());
                exploreUsername.setText(userNameDatabase);
                descriptionUsername.setText(userNameDatabase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        CardView explore = (CardView) findViewById(R.id.explore);
        explore.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, explore_iea.class)));

        logoutImg.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
        });

        coreMembersCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, CoreTeamMembers.class)));

        memberDirectoryCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, MembersDirectory.class)));

        grievanceCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, Grievance.class)));

        newMembers.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, member_approval.class)));



        contactUs.setOnClickListener(view -> {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint("InflateParams") View exploreUsView = inflater.inflate(R.layout.support_contact_popup, null);

            exploreIeaContactDialog.setContentView(exploreUsView);
            exploreIeaContactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            exploreIeaContactDialog.show();
        });

        userImage.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, UserProfile.class)));

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}