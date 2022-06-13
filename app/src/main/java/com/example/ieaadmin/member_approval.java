package com.example.ieaadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

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
        memberApprovalRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        memberApproval_babkbtn = findViewById(R.id.members_approval_back_button);

        FirebaseRecyclerOptions<UserRegistrationHelperClass> options =
                new FirebaseRecyclerOptions.Builder<UserRegistrationHelperClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Temp Registry"), UserRegistrationHelperClass.class)
                        .build();

        approvalAdapter = new member_approvalAdapter(options);
        memberApprovalRecyclerView.setAdapter(approvalAdapter);


        memberApproval_babkbtn.setOnClickListener(view -> finish());



    }

    public class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
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