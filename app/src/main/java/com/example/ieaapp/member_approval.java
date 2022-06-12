package com.example.ieaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class member_approval extends AppCompatActivity {

    RecyclerView memberApprovalRecyclerView;
    member_approvalAdapter approvalAdapter;
    AppCompatButton memberApproval_babkbtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_approval);

        memberApprovalRecyclerView = (RecyclerView) findViewById(R.id.members_approval_recycler_view);
        memberApprovalRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        memberApproval_babkbtn = findViewById(R.id.members_approval_back_button);

        FirebaseRecyclerOptions<UserRegistrationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<UserRegistrationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Temp Registry"), UserRegistrationHelperClass.class)
                        .build();

        approvalAdapter = new member_approvalAdapter(options);
        memberApprovalRecyclerView.setAdapter(approvalAdapter);


        memberApproval_babkbtn.setOnClickListener(view -> finish());



    }

    @Override
    protected void onStart() {
        super.onStart();
        approvalAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        approvalAdapter.stopListening();
    }
}