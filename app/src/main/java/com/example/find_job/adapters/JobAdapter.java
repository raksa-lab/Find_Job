package com.example.find_job.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.Job;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Job> jobList;

    // CLICK
    private OnJobClickListener clickListener;

    // LONG PRESS
    private OnJobLongClickListener longClickListener;

    // =========================
    // CONSTRUCTOR
    // =========================
    public JobAdapter(List<Job> jobList, OnJobClickListener clickListener) {
        this.jobList = jobList;
        this.clickListener = clickListener;
    }

    // =========================
    // SET LONG CLICK LISTENER
    // =========================
    public void setOnJobLongClickListener(OnJobLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        // =========================
        // BASIC INFO
        // =========================
        holder.title.setText(job.title);
        holder.company.setText(job.company);
        holder.location.setText(job.location);

        // =========================
        // SALARY
        // =========================
        holder.salary.setText(
                job.salary > 0 ? "$" + job.salary + " / m" : "Negotiable"
        );

        // =========================
        // EMPLOYMENT TYPE
        // =========================
        if (job.employmentType != null && !job.employmentType.isEmpty()) {
            holder.jobType.setText(formatJobType(job.employmentType));
            holder.jobType.setVisibility(View.VISIBLE);
        } else {
            holder.jobType.setVisibility(View.GONE);
        }

        // =========================
        // REQUIREMENTS PREVIEW
        // =========================
        if (job.requirements != null && !job.requirements.isEmpty()) {
            holder.requirementsSmall.setText(
                    TextUtils.join(" • ", job.requirements)
            );
        } else {
            holder.requirementsSmall.setText("No requirements");
        }

        // =========================
        // CLICK → OPEN DETAIL
        // =========================
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onJobClick(job);
            }
        });

        // =========================
        // LONG PRESS → REMOVE / ACTION
        // =========================
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onJobLongClick(job);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return jobList == null ? 0 : jobList.size();
    }

    // =========================
    // FORMAT JOB TYPE
    // =========================
    private String formatJobType(String type) {
        switch (type.toLowerCase()) {
            case "full-time":
            case "full_time":
                return "Full-Time";
            case "part-time":
            case "part_time":
                return "Part-Time";
            case "intern":
            case "internship":
                return "Intern";
            default:
                return type;
        }
    }

    // =========================
    // INTERFACES
    // =========================
    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    public interface OnJobLongClickListener {
        void onJobLongClick(Job job);
    }

    // =========================
    // VIEW HOLDER
    // =========================
    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView title, company, location, salary;
        TextView jobType, requirementsSmall;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvJobTitle);
            company = itemView.findViewById(R.id.tvCompany);
            location = itemView.findViewById(R.id.tvLocation);
            salary = itemView.findViewById(R.id.tvSalary);
            jobType = itemView.findViewById(R.id.tvJobType);
            requirementsSmall = itemView.findViewById(R.id.tvRequirementsSmall);
        }
    }
}
