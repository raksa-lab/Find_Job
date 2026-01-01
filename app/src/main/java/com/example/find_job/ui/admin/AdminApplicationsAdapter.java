package com.example.find_job.ui.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.AdminApplication;

import java.util.List;

public class AdminApplicationsAdapter
        extends RecyclerView.Adapter<AdminApplicationsAdapter.ViewHolder> {

    public interface OnAction {
        void onApprove(AdminApplication app);
        void onReject(AdminApplication app);
        void onDelete(AdminApplication app); // ✅ REQUIRED
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

        h.tvJob.setText(app.jobTitle);
        h.tvCompany.setText(app.jobCompany);
        h.tvUser.setText(app.userName);
        h.tvStatus.setText(app.status);

        h.btnApprove.setOnClickListener(v ->
                listener.onApprove(app)
        );

        h.btnReject.setOnClickListener(v ->
                listener.onReject(app)
        );

        h.btnDelete.setOnClickListener(v ->
                listener.onDelete(app)
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvJob, tvCompany, tvUser, tvStatus;
        Button btnApprove, btnReject, btnDelete;

        ViewHolder(@NonNull View v) {
            super(v);
            tvJob = v.findViewById(R.id.tvJobTitle);
            tvCompany = v.findViewById(R.id.tvCompany);
            tvUser = v.findViewById(R.id.tvUser);
            tvStatus = v.findViewById(R.id.tvStatus);

            btnApprove = v.findViewById(R.id.btnApprove);
            btnReject = v.findViewById(R.id.btnReject);
            btnDelete = v.findViewById(R.id.btnDelete); // ✅ REQUIRED
        }
    }
}
