package com.example.ieaapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CoreMemberAdapter extends FirebaseRecyclerAdapter<CoreMemberModel, CoreMemberAdapter.CoreMemberViewHolder> {



    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CoreMemberAdapter(@NonNull FirebaseRecyclerOptions<CoreMemberModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CoreMemberViewHolder holder, int position, @NonNull CoreMemberModel model) {
        holder.name.setText(model.getName());
        holder.companyName.setText(model.getCompany_name());

        Glide.with(holder.img.getContext())
                .load(model.getPurl())
                .placeholder(R.drawable.iea_logo)
                .circleCrop()
                .error(R.drawable.iea_logo)
                .into(holder.img);

        holder.coreMemberView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CoreMemberDetail.class);
            intent.putExtra("CoreItemKey", getRef(position).getKey());
            view.getContext().startActivity(intent);
        });

        holder.detailButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), CoreMemberDetail.class);
            intent.putExtra("CoreItemKey", getRef(position).getKey());
            view.getContext().startActivity(intent);
        });
}

    @NonNull
    @Override
    public CoreMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.core_team_member_item,parent,false);
        return new CoreMemberViewHolder(view);
    }

    class CoreMemberViewHolder extends RecyclerView.ViewHolder{

        View coreMemberView;
        ImageView img;
        TextView name, companyName;
        Button detailButton;
//        View coreMemberView;

        public CoreMemberViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.core_team_member_profile_picture);
            name = (TextView) itemView.findViewById(R.id.itemCoreMemberNameText);
            companyName = (TextView) itemView.findViewById(R.id.itemCompanyName);
            coreMemberView = itemView;
            detailButton = (Button) itemView.findViewById(R.id.core_team_member_detail_button);
        }
    }
}
