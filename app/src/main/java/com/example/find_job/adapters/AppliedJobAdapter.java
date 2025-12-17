package com.example.find_job.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.job_detail.JobDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class AppliedJobAdapter
        extends RecyclerView.Adapter<AppliedJobAdapter.ViewHolder> {

    private final Context context;
    private final List<Job> list;

    public AppliedJobAdapter(Context context, List<Job> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_applied_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder h,
            int position
    ) {
        Job job = list.get(position);

        // ===== BASIC INFO =====
        h.tvTitle.setText(job.title);
        h.tvCompany.setText(job.company);
        h.tvLocation.setText(job.location);

        // ===== SALARY =====
        if (job.salary > 0) {
            h.tvSalary.setText("$" + job.salary + " / m");
        } else {
            h.tvSalary.setText("Negotiable");
        }

        // ===== EMPLOYMENT TYPE =====
        h.tvJobType.setText(
                job.employmentType != null
                        ? job.employmentType
                        : "—"
        );

        // ===== REQUIREMENTS (PREVIEW) =====
        if (job.requirements != null && !job.requirements.isEmpty()) {
            h.tvRequirements.setText(
                    TextUtils.join(" • ", job.requirements)
            );
        } else {
            h.tvRequirements.setText("No requirements listed");
        }

        // ===== OPEN DETAIL =====
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, JobDetailActivity.class);

            i.putExtra("fromApplied", true);
            i.putExtra("jobId", job.id);
            i.putExtra("title", job.title);
            i.putExtra("company", job.company);
            i.putExtra("location", job.location);
            i.putExtra("description", job.description);
            i.putExtra("salary", job.salary);

            // 🔴 ADD THIS LINE (FIX)
            i.putExtra("employmentType", job.employmentType);

            i.putStringArrayListExtra(
                    "requirements",
                    new ArrayList<>(job.requirements)
            );

            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCompany, tvLocation;
        TextView tvSalary, tvJobType, tvRequirements;

        ViewHolder(@NonNull View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvJobTitle);
            tvCompany = v.findViewById(R.id.tvCompany);
            tvLocation = v.findViewById(R.id.tvLocation);
            tvSalary = v.findViewById(R.id.tvSalary);
            tvJobType = v.findViewById(R.id.tvJobType);
            tvRequirements = v.findViewById(R.id.tvRequirementsSmall);
        }
    }
}
