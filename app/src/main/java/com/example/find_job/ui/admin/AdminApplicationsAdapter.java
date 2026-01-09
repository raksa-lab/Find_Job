package com.example.find_job.ui.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AdminApplication;
import com.example.find_job.ui.admin.application.AdminApplicationDetailActivity;

import java.util.List;

public class AdminApplicationsAdapter
        extends RecyclerView.Adapter<AdminApplicationsAdapter.ViewHolder> {

    // ===============================
    // ACTION INTERFACE (FIXED)
    // ===============================
    public interface OnAction {
        void onApprove(AdminApplication app);
        void onReject(AdminApplication app);
        void onDelete(AdminApplication app);
        void onReply(AdminApplication app);   // ✅ FIXED
    }

    private final Context context;
    private final List<AdminApplication> list;
    private final OnAction listener;

    public AdminApplicationsAdapter(
            Context context,
            List<AdminApplication> list,
            OnAction listener
    ) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_admin_application, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder h,
            int position
    ) {
        AdminApplication app = list.get(position);

        // ===============================
        // BIND BASIC DATA
        // ===============================
        h.tvJob.setText(app.jobTitle != null ? app.jobTitle : "—");
        h.tvCompany.setText(app.jobCompany != null ? app.jobCompany : "—");
        h.tvUser.setText(app.userName != null ? app.userName : "—");
        h.tvStatus.setText(
                app.status != null ? app.status.toUpperCase() : "UNKNOWN"
        );

        // ===============================
        // OPEN DETAIL
        // ===============================
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(
                    context,
                    AdminApplicationDetailActivity.class
            );
            i.putExtra("app_id", app.id);
            context.startActivity(i);
        });

        // ===============================
        // BUTTON ACTIONS
        // ===============================
        h.btnApprove.setOnClickListener(v ->
                listener.onApprove(app)
        );

        h.btnReject.setOnClickListener(v ->
                listener.onReject(app)
        );

        h.btnDelete.setOnClickListener(v ->
                listener.onDelete(app)
        );

        h.btnReply.setOnClickListener(v ->
                listener.onReply(app)   // ✅ FIXED
        );
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    // ===============================
    // VIEW HOLDER
    // ===============================
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvJob, tvCompany, tvUser, tvStatus;
        Button btnApprove, btnReject, btnDelete, btnReply;

        ViewHolder(@NonNull View v) {
            super(v);

            tvJob = v.findViewById(R.id.tvJobTitle);
            tvCompany = v.findViewById(R.id.tvCompany);
            tvUser = v.findViewById(R.id.tvUser);
            tvStatus = v.findViewById(R.id.tvStatus);

            btnApprove = v.findViewById(R.id.btnApprove);
            btnReject = v.findViewById(R.id.btnReject);
            btnDelete = v.findViewById(R.id.btnDelete);
            btnReply = v.findViewById(R.id.btnReply); // ✅ FIXED
        }
    }
}
