package com.example.find_job.ui.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.job_detail.JobDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private RecyclerView rvHomeJobs;
    private JobAdapter adapter;
    private HomeViewModel viewModel;

    public Home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);

        rvHomeJobs = view.findViewById(R.id.rvHomeJobs);
        rvHomeJobs.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new androidx.lifecycle.ViewModelProvider(requireActivity())
                .get(HomeViewModel.class);

        viewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs == null) return;

            // Limit to only 3 jobs
            List<Job> limitedJobs = new ArrayList<>();
            int limit = Math.min(jobs.size(), 3);
            for (int i = 0; i < limit; i++) {
                limitedJobs.add(jobs.get(i));
            }

            adapter = new JobAdapter(limitedJobs, job -> openJobDetail(job));
            rvHomeJobs.setAdapter(adapter);
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
        startActivity(i);
    }
}