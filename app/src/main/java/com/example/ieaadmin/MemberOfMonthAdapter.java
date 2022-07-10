package com.example.ieaadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MemberOfMonthAdapter extends FirebaseRecyclerAdapter<MembersDirectoryModel,MemberOfMonthAdapter.memberOfmonthViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MemberOfMonthAdapter(@NonNull FirebaseRecyclerOptions<MembersDirectoryModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull memberOfmonthViewHolder holder, int position, @NonNull MembersDirectoryModel model) {
        holder.name.setText(model.getName());
        holder.company.setText(model.getCompany_name());

        Glide.with(holder.img.getContext())
                .load(model.getPurl())
                .placeholder(R.drawable.iea_logo)
                .circleCrop()
                .error(R.drawable.iea_logo)
                .into(holder.img);

        holder.memberofmonthView.setOnClickListener(view -> {
            ProgressDialog progressDialog = new ProgressDialog (view.getContext());
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            DatabaseReference memberRef = FirebaseDatabase.getInstance().getReference().child("Member of Month");
            MemberofMonthModel newMOM = new MemberofMonthModel(model.getName(), model.getCompany_name(), model.getPurl());
            memberRef.setValue(newMOM).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    Toast.makeText(view.getContext(), "All Done", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(view.getContext(), "Try again", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

    @NonNull
    @Override
    public memberOfmonthViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.core_team_member_item,parent,false);
        return new memberOfmonthViewHolder(view);
    }

    class memberOfmonthViewHolder extends RecyclerView.ViewHolder{

        View memberofmonthView;
        ImageView img;
        TextView name,company;
        Button detailButton;

        public memberOfmonthViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.core_team_member_profile_picture);
            name = (TextView) itemView.findViewById(R.id.itemCoreMemberNameText);
            company = (TextView)itemView.findViewById(R.id.itemCoreMemberDesignationText);
            memberofmonthView = itemView;
            detailButton = (Button) itemView.findViewById(R.id.core_team_member_detail_button);
        }
    }
}
