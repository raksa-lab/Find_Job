package com.example.find_job.ui.job_list;

import android.content.Intent;
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
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.job_detail.JobDetailActivity;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class JobListFragment extends Fragment {

    private JobListViewModel viewModel;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private ChipGroup chipGroup;

    // Cache jobs for filtering
    private final List<Job> allJobs = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.activity_job_list, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvAllJobs);
        chipGroup = view.findViewById(R.id.category_chips);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity())
                .get(JobListViewModel.class);

        // Observe job list
        viewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs == null) return;

            allJobs.clear();
            allJobs.addAll(jobs);

            filterJobsByChip(chipGroup.getCheckedChipId());
        });

        // Chip filtering
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            filterJobsByChip(checkedId);
        });

        // 🔥 INITIAL LOAD
        viewModel.refreshJobs();
    }

    // 🔥 REFRESH WHEN RETURNING FROM ADD JOB
    @Override
    public void onResume() {
        super.onResume();
        viewModel.refreshJobs();
    }

    // =================================================
    // FILTER JOBS BY CHIP
    // =================================================
    private void filterJobsByChip(int chipId) {

        if (chipId == R.id.chip_all || chipId == View.NO_ID) {
            jobAdapter = new JobAdapter(allJobs, this::openJobDetail);
            recyclerView.setAdapter(jobAdapter);
            return;
        }

        List<String> keywords = getKeywordsForChip(chipId);
        List<Job> filtered = new ArrayList<>();

        for (Job job : allJobs) {
            if (job.tags == null) continue;

            for (String tag : job.tags) {
                for (String key : keywords) {
                    if (tag.equalsIgnoreCase(key)) {
                        filtered.add(job);
                        break;
                    }
                }
            }
        }

        jobAdapter = new JobAdapter(filtered, this::openJobDetail);
        recyclerView.setAdapter(jobAdapter);
    }

    // =================================================
    // CHIP → TAG KEYWORDS
    // =================================================
    private List<String> getKeywordsForChip(int chipId) {

        List<String> list = new ArrayList<>();

        if (chipId == R.id.chip_design) {
            list.add("design");
            list.add("uiux");
            list.add("graphic");
            list.add("figma");
        }
        else if (chipId == R.id.chip_business) {
            list.add("business");
            list.add("sales");
            list.add("crm");
        }
        else if (chipId == R.id.chip_marketing) {
            list.add("marketing");
            list.add("seo");
            list.add("content");
        }
        else if (chipId == R.id.chip_technology) {
            list.add("software");
            list.add("backend");
            list.add("frontend");
            list.add("it");
            list.add("api");
        }
        else if (chipId == R.id.chip_finance) {
            list.add("finance");
            list.add("accounting");
        }

        return list;
    }

    // =================================================
    // OPEN JOB DETAIL
    // =================================================
    private void openJobDetail(Job job) {

        Intent i = new Intent(getActivity(), JobDetailActivity.class);

        i.putExtra("jobId", job.id);
        i.putExtra("title", job.title);
        i.putExtra("company", job.company);
        i.putExtra("location", job.location);
        i.putExtra("description", job.description);
        i.putExtra("salary", job.salary);
        i.putExtra("employmentType", job.employmentType);
        i.putExtra("status", job.status);

        if (job.createdAt != null) {
            i.putExtra("createdAt_seconds", job.createdAt._seconds);
        }

        i.putStringArrayListExtra(
                "requirements",
                new ArrayList<>(job.requirements)
        );

        startActivity(i);
    }
}
