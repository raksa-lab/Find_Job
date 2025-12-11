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
import com.example.find_job.data.models.Job;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.ui.job_detail.JobDetailActivity;

public class JobListFragment extends Fragment {

    private JobListViewModel viewModel;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_job_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvAllJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(requireActivity()).get(JobListViewModel.class);

        viewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs == null) return;

            if (jobAdapter == null) {
                jobAdapter = new JobAdapter(jobs, job -> openJobDetail(job));
                recyclerView.setAdapter(jobAdapter);
            }
        });
    }

    private void openJobDetail(Job job) {
        Intent i = new Intent(getActivity(), JobDetailActivity.class);

        i.putExtra("jobId", job.id);
        i.putExtra("title", job.title);
        i.putExtra("company", job.company);
        i.putExtra("location", job.location);
        i.putExtra("description", job.description);
        i.putExtra("salary", job.salary);
        i.putExtra("employmentType", job.employmentType);

        // NEW FIELDS
        i.putExtra("status", job.status);

        if (job.createdAt != null) {
            i.putExtra("createdAt_seconds", job.createdAt._seconds);
        }

        startActivity(i);
    }

}

