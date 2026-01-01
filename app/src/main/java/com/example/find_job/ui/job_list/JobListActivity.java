package com.example.find_job.ui.job_list;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.job_detail.JobDetailActivity;

import java.util.ArrayList;

public class JobListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private JobListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        recyclerView = findViewById(R.id.rvAllJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this)
                .get(JobListViewModel.class);

        viewModel.getJobs().observe(this, jobs -> {

            jobAdapter = new JobAdapter(
                    this,               // ✅ Context
                    jobs,               // ✅ Job list
                    this::openJobDetail // ✅ Card click only
            );

            recyclerView.setAdapter(jobAdapter);
        });

    }

    private void openJobDetail(Job job) {
        Intent i = new Intent(this, JobDetailActivity.class);

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

        ArrayList<String> reqList = new ArrayList<>();
        if (job.requirements != null) {
            reqList.addAll(job.requirements);
        }
        i.putStringArrayListExtra("requirements", reqList);

        startActivity(i);
    }
}
