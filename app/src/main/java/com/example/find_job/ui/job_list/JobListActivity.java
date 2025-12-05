package com.example.find_job.ui.job_list;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;

public class JobListActivity extends AppCompatActivity {

    private JobListViewModel viewModel;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        recyclerView = findViewById(R.id.rvAllJobs);

        viewModel = new ViewModelProvider(this).get(JobListViewModel.class);

        viewModel.getJobs().observe(this, jobs -> {
            jobAdapter = new JobAdapter(jobs);
            recyclerView.setAdapter(jobAdapter);
        });
    }
}
