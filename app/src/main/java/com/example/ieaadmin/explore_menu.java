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

import de.hdodenhof.circleimageview.CircleImageView;

public class explore_menu extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView exploreUsername, Memberofmonthname;
    ImageView logoutImg, userImage;
    CircleImageView MemberofmonthImg;
    CardView  memberDirectoryCard, grievanceCard, newMembers,bbas,event;
    Dialog exploreIeaContactDialog;
    DatabaseReference databaseReference;
    DatabaseReference MemberOfMonthref = FirebaseDatabase.getInstance().getReference("Member of Month");
    StorageReference storageProfilePicReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_menu);

        exploreUsername = findViewById(R.id.explore_username);
        Memberofmonthname = findViewById(R.id.description_username);
        MemberofmonthImg = findViewById(R.id.description_img);
        logoutImg = findViewById(R.id.logout_img);
        memberDirectoryCard = findViewById(R.id.member_directory);
        grievanceCard = findViewById(R.id.grievance);
        newMembers = findViewById(R.id.new_member);
        bbas = findViewById(R.id.bbas);
        exploreIeaContactDialog = new Dialog(this);
        userImage = findViewById(R.id.user_img);
        event = findViewById(R.id.events);

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        MemberOfMonthref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String UserNameStr = Objects.requireNonNull(snapshot.child("name").getValue().toString());
                String purl = Objects.requireNonNull(snapshot.child("purl").getValue().toString());
                Memberofmonthname.setText(UserNameStr);
                Glide.with(getApplicationContext())
                        .load(purl)
                        .placeholder(R.drawable.iea_logo)
                        .circleCrop()
                        .error(R.drawable.iea_logo)
                        .into(MemberofmonthImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        logoutImg.setOnClickListener(view -> {
            mAuth.signOut();
            finish();
        });


        memberDirectoryCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, MembersDirectory.class)));

        MemberofmonthImg.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, MemberOfMonth.class)));

        grievanceCard.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, Grievance.class)));

        newMembers.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, member_approval.class)));

        event.setOnClickListener(view -> startActivity(new Intent(explore_menu.this, EventList.class)));

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