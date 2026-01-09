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

import java.util.ArrayList;
import java.util.List;

public class AdminApplicationsAdapter
        extends RecyclerView.Adapter<AdminApplicationsAdapter.ViewHolder> {

    public interface OnAction {
        void onApprove(AdminApplication app);
        void onReject(AdminApplication app);
        void onDelete(AdminApplication app);
        void onReply(AdminApplication app);
    }

    private final Context context;
    private final OnAction listener;
    private final List<AdminApplication> list = new ArrayList<>();

    public AdminApplicationsAdapter(Context context, OnAction listener) {
        this.context = context;
        this.listener = listener;
    }

    // ✅ REQUIRED
    public void update(List<AdminApplication> data) {
        list.clear();
        if (data != null) list.addAll(data);
        notifyDataSetChanged();
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

        // JOB TITLE
        String title = app.jobTitle;
        if (title == null && app.job != null) {
            title = app.job.title;
        }
        h.tvJob.setText(title != null ? title : "—");

        // COMPANY
        String company = app.jobCompany;
        if (company == null && app.job != null) {
            company = app.job.company;
        }
        h.tvCompany.setText(company != null ? company : "—");

        // USER NAME (✔ SAFE)
        h.tvUser.setText(
                app.userName != null ? app.userName : "—"
        );

        // STATUS
        h.tvStatus.setText(
                app.status != null ? app.status.toUpperCase() : "UNKNOWN"
        );

        // OPEN DETAIL
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(
                    context,
                    AdminApplicationDetailActivity.class
            );
            i.putExtra("app_id", app.id);
            context.startActivity(i);
        });

        // ACTIONS
        h.btnApprove.setOnClickListener(v -> listener.onApprove(app));
        h.btnReject.setOnClickListener(v -> listener.onReject(app));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(app));
        h.btnReply.setOnClickListener(v -> listener.onReply(app));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

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
            btnReply = v.findViewById(R.id.btnReply);
        }
    }
}
