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

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
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
        holder.salary.setText(String.valueOf(job.salary));

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView title, company, location, salary;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvJobTitle);
            company = itemView.findViewById(R.id.tvCompany);
            location = itemView.findViewById(R.id.tvLocation);
            salary = itemView.findViewById(R.id.tvSalary);
        }
    }
}
