package com.example.find_job.ui.application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.adapters.AppliedJobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AppliedJobsFragment extends Fragment {

    private RecyclerView rv;
    private AppliedJobAdapter adapter;
    private JobRepository repository;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.applied_jobs, container, false);

        rv = view.findViewById(R.id.rvAppliedJobs);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        repository = new JobRepository();

        loadAppliedJobs();

        return view;
    }

    private void loadAppliedJobs() {
        SharedPreferences prefs =
                requireContext().getSharedPreferences("APPLIED_JOBS", MODE_PRIVATE);

        Set<String> appliedJobIds = prefs.getAll().keySet();

        repository.fetchJobs().observe(getViewLifecycleOwner(), jobs -> {

            List<Job> appliedJobs = new ArrayList<>();

            for (Job job : jobs) {
                if (appliedJobIds.contains(job.id)) {
                    appliedJobs.add(job);
                }
            }

            adapter = new AppliedJobAdapter(requireContext(), appliedJobs);
            rv.setAdapter(adapter);
        });
    }
}
