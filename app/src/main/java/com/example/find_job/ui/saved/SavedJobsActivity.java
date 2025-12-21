package com.example.find_job.ui.saved;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;
import com.example.find_job.ui.base.BaseAuthActivity;
import com.example.find_job.ui.job_detail.JobDetailActivity;
import com.example.find_job.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SavedJobsActivity extends BaseAuthActivity {

    private RecyclerView recyclerView;
    private JobAdapter adapter;
    private JobRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🚫 ADMIN SHOULD NOT ACCESS SAVED JOBS
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isAdmin()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_saved_jobs);

        View btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        recyclerView = findViewById(R.id.rvSavedJobs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = new JobRepository(this);
        loadSavedJobs();
    }

    // =========================
    // LOAD SAVED JOBS
    // =========================
    private void loadSavedJobs() {

        SharedPreferences prefs =
                getSharedPreferences("saved_jobs", MODE_PRIVATE);

        Set<String> savedIds =
                prefs.getStringSet("job_ids", new HashSet<>());

        repository.fetchJobs().observe(this, jobs -> {

            List<Job> savedJobs = new ArrayList<>();

            for (Job job : jobs) {
                if (savedIds.contains(job.id)) {
                    savedJobs.add(job);
                }
            }

            adapter = new JobAdapter(savedJobs, this::openDetail);

            adapter.setOnJobLongClickListener(job -> {
                showRemoveDialog(job.id);
            });

            recyclerView.setAdapter(adapter);
        });
    }

    // =========================
    // REMOVE SAVED JOB
    // =========================
    private void showRemoveDialog(String jobId) {
        new AlertDialog.Builder(this)
                .setTitle("Remove saved job?")
                .setMessage("This job will be removed from saved list.")
                .setPositiveButton("Remove",
                        (d, w) -> removeSavedJob(jobId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void removeSavedJob(String jobId) {
        SharedPreferences prefs =
                getSharedPreferences("saved_jobs", MODE_PRIVATE);

        Set<String> saved =
                prefs.getStringSet("job_ids", new HashSet<>());

        Set<String> updated = new HashSet<>(saved);
        updated.remove(jobId);

        prefs.edit().putStringSet("job_ids", updated).apply();

        loadSavedJobs();
    }

    // =========================
    // OPEN JOB DETAIL
    // =========================
    private void openDetail(Job job) {
        Intent i = new Intent(this, JobDetailActivity.class);

        i.putExtra("jobId", job.id);
        i.putExtra("title", job.title);
        i.putExtra("company", job.company);
        i.putExtra("location", job.location);
        i.putExtra("description", job.description);
        i.putExtra("salary", job.salary);
        i.putExtra("employmentType", job.employmentType);

        if (job.requirements != null) {
            i.putStringArrayListExtra(
                    "requirements",
                    new ArrayList<>(job.requirements)
            );
        }

        startActivity(i);
    }
}
