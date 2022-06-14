package com.example.ieaadmin;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ieaadmin.MembersDirectoryAdapter;
import com.example.ieaadmin.MembersDirectoryModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MembersDirectory extends AppCompatActivity{

    RecyclerView memberDirectoryRecyclerView;
    AppCompatButton memberDirectoryBackButton;
    MembersDirectoryAdapter memberDirectoryAdapter;
    FirebaseRecyclerOptions<MembersDirectoryModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_directory);

        memberDirectoryRecyclerView = (RecyclerView) findViewById(R.id.members_directory_recycler_view);
        memberDirectoryRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        memberDirectoryBackButton = findViewById(R.id.members_directory_back_button);

        options = new FirebaseRecyclerOptions.Builder<MembersDirectoryModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Registered Users"), MembersDirectoryModel.class)
                .build();

        memberDirectoryAdapter = new MembersDirectoryAdapter(options);
        memberDirectoryRecyclerView.setAdapter(memberDirectoryAdapter);
        memberDirectoryBackButton.setOnClickListener(view -> finish());
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
        memberDirectoryAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        memberDirectoryAdapter.stopListening();
    }

}