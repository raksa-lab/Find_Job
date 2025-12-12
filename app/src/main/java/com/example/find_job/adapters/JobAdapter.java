package com.example.find_job.adapters;

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
    private OnJobClickListener listener;

    public JobAdapter(List<Job> jobList, OnJobClickListener listener) {
        this.jobList = jobList;
        this.listener = listener;
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

        holder.title.setText(job.title);
        holder.company.setText(job.company);
        holder.location.setText(job.location);
        holder.salary.setText("$" + job.salary);

        // Show first requirement
        if (job.requirements != null && !job.requirements.isEmpty()) {
            holder.requirementsSmall.setText("• " + job.requirements.get(0));
        } else {
            holder.requirementsSmall.setText("No requirements");
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onJobClick(job);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView title, company, location, salary, requirementsSmall;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvJobTitle);
            company = itemView.findViewById(R.id.tvCompany);
            location = itemView.findViewById(R.id.tvLocation);
            salary = itemView.findViewById(R.id.tvSalary);
            requirementsSmall = itemView.findViewById(R.id.tvRequirementsSmall);
        }
    }
}
