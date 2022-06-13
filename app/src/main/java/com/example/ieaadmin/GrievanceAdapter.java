package com.example.ieaadmin;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class GrievanceAdapter extends FirebaseRecyclerAdapter<GrievanceModel, GrievanceAdapter.GrievanceViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GrievanceAdapter(@NonNull FirebaseRecyclerOptions<GrievanceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GrievanceViewHolder holder, int position, @NonNull GrievanceModel model) {
        holder.grievanceEmail.setText(model.getEmail());
        holder.grievanceDepartmentTv.setText(model.getDepartment());

        switch (model.getStatus()){
            case "Unsolved":
                holder.grievanceStatusTv.setBackgroundColor(Color.parseColor("#96271f"));
                break;
            case "On Progress":
                holder.grievanceStatusTv.setBackgroundColor(Color.parseColor("#ED944D"));
                break;
            case "Solved":
                holder.grievanceStatusTv.setBackgroundColor(Color.parseColor("#48A14D"));
        }

        holder.grievanceView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), GrievanceDetail.class);
            intent.putExtra("GrievanceItemKey", getRef(position).getKey());
            view.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public GrievanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewGrievance = LayoutInflater.from(parent.getContext()).inflate(R.layout.grievance_item,parent,false);
        return new GrievanceViewHolder(viewGrievance);
    }

    public class GrievanceViewHolder extends RecyclerView.ViewHolder {

        View grievanceView;
        TextView grievanceEmail, grievanceDepartmentTv, grievanceStatusTv;
        public GrievanceViewHolder(@NonNull View itemView) {
            super(itemView);

            grievanceEmail = (TextView) itemView.findViewById(R.id.grievance_email_tv);
            grievanceDepartmentTv = (TextView) itemView.findViewById(R.id.grievance_department_tv);
            grievanceStatusTv = (TextView) itemView.findViewById(R.id.grievance_status_color_tv);
            grievanceView = itemView;
        }
    }
}
