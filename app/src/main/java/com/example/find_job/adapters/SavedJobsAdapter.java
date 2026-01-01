package com.example.find_job.adapters;

import android.view.*;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.data.models.Job;

import java.util.*;

public class SavedJobsAdapter
        extends RecyclerView.Adapter<SavedJobsAdapter.VH> {

    private final List<Job> jobs;
    private final Set<String> selectedIds = new HashSet<>();

    public SavedJobsAdapter(List<Job> jobs) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_job, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Job job = jobs.get(pos);

        h.cb.setVisibility(View.VISIBLE);
        h.cb.setChecked(selectedIds.contains(job.id));

        h.title.setText(job.title);
        h.company.setText(job.company);

        h.itemView.setOnClickListener(v -> {
            toggle(job.id);
            notifyItemChanged(pos);
        });

        h.cb.setOnClickListener(v -> toggle(job.id));
    }

    private void toggle(String id) {
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
    }

    public List<String> getSelectedIds() {
        return new ArrayList<>(selectedIds);
    }

    public void clearSelection() {
        selectedIds.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, company;
        CheckBox cb;

        VH(View v) {
            super(v);
            title = v.findViewById(R.id.tvJobTitle);
            company = v.findViewById(R.id.tvCompany);
            cb = v.findViewById(R.id.cbSelect);
        }
    }
}
