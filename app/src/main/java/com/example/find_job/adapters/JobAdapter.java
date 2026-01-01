package com.example.find_job.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.FavoriteRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private final Context context;
    private final List<Job> jobList;
    private final OnJobClickListener jobClickListener;
    private final FavoriteRepository favoriteRepository;

    // ===== ADMIN SELECTION MODE =====
    private boolean selectionEnabled = false;
    private final Set<String> selectedIds = new HashSet<>();

    // ===== CONSTRUCTOR =====
    public JobAdapter(
            Context context,
            List<Job> jobs,
            OnJobClickListener jobClickListener
    ) {
        this.context = context;
        this.jobList = jobs != null ? jobs : new ArrayList<>();
        this.jobClickListener = jobClickListener;
        this.favoriteRepository = new FavoriteRepository(context);
    }

    // =============================
    // SELECTION MODE (ADMIN)
    // =============================
    public void enableSelection(boolean enable) {
        selectionEnabled = enable;
        selectedIds.clear();
        notifyDataSetChanged();
    }

    public List<String> getSelectedJobIds() {
        return new ArrayList<>(selectedIds);
    }

    public void clearSelection() {
        selectedIds.clear();
        notifyDataSetChanged();
    }

    // =============================
    // UPDATE DATA
    // =============================
    public void updateData(List<Job> newJobs) {
        jobList.clear();
        if (newJobs != null) jobList.addAll(newJobs);
        selectedIds.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull JobViewHolder holder,
            int position
    ) {
        Job job = jobList.get(position);

        // ===== BASIC INFO =====
        holder.title.setText(job.title);
        holder.company.setText(job.company);
        holder.location.setText(job.location);

        holder.salary.setText(
                job.salary > 0 ? "$" + job.salary + " / m" : "Negotiable"
        );

        holder.jobType.setText(formatJobType(job.employmentType));

        holder.requirementsSmall.setText(
                job.requirements != null && !job.requirements.isEmpty()
                        ? TextUtils.join(" • ", job.requirements)
                        : "No requirements"
        );

        // =============================
        // BOOKMARK STATE
        // =============================
        boolean isSaved = favoriteRepository.isFavorite(job.id);

        holder.ivBookmark.setImageResource(
                isSaved
                        ? R.drawable.ic_bookmark_filled
                        : R.drawable.ic_bookmark_border
        );

        // =============================
        // BOOKMARK CLICK (REAL SAVE)
        // =============================
        holder.bookmarkButton.setOnClickListener(v -> {

            boolean savedNow = favoriteRepository.isFavorite(job.id);

            if (!savedNow) {
                favoriteRepository.addFavorite(job.id);
                holder.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                Toast.makeText(
                        context,
                        "Saved for later",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                favoriteRepository.removeFavorite(job.id);
                holder.ivBookmark.setImageResource(R.drawable.ic_bookmark_border);
                Toast.makeText(
                        context,
                        "Removed from saved",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        // Prevent card click when tapping bookmark
        holder.bookmarkButton.setOnTouchListener((v, e) -> {
            v.performClick();
            return true;
        });

        // =============================
        // SELECTION MODE (ADMIN)
        // =============================
        if (selectionEnabled) {
            holder.checkBox.setVisibility(View.VISIBLE);

            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(selectedIds.contains(job.id));

            holder.checkBox.setOnCheckedChangeListener((b, checked) -> {
                if (checked) {
                    selectedIds.add(job.id);
                } else {
                    selectedIds.remove(job.id);
                }
            });

            holder.itemView.setOnClickListener(v ->
                    holder.checkBox.performClick()
            );

        } else {
            holder.checkBox.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(v -> {
                if (jobClickListener != null) {
                    jobClickListener.onJobClick(job);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    // =============================
    // HELPERS
    // =============================
    private String formatJobType(String type) {
        if (type == null) return "";
        switch (type.toLowerCase()) {
            case "full-time":
                return "Full-Time";
            case "part-time":
                return "Part-Time";
            case "intern":
            case "internship":
                return "Intern";
            default:
                return type;
        }
    }

    // =============================
    // LISTENER
    // =============================
    public interface OnJobClickListener {
        void onJobClick(Job job);
    }

    // =============================
    // VIEW HOLDER
    // =============================
    static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView title, company, location, salary, jobType, requirementsSmall;
        CheckBox checkBox;
        FrameLayout bookmarkButton;
        ImageView ivBookmark;

        JobViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvJobTitle);
            company = itemView.findViewById(R.id.tvCompany);
            location = itemView.findViewById(R.id.tvLocation);
            salary = itemView.findViewById(R.id.tvSalary);
            jobType = itemView.findViewById(R.id.tvJobType);
            requirementsSmall = itemView.findViewById(R.id.tvRequirementsSmall);
            checkBox = itemView.findViewById(R.id.cbSelect);
            bookmarkButton = itemView.findViewById(R.id.bookmark_button);
            ivBookmark = itemView.findViewById(R.id.ivBookmark);
        }
    }
}
