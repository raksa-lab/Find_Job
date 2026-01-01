package com.example.find_job.ui.saved;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_job.R;
import com.example.find_job.adapters.JobAdapter;
import com.example.find_job.data.models.Job;
import com.example.find_job.ui.base.BaseAuthActivity;
import com.example.find_job.utils.SessionManager;

import java.util.ArrayList;

public class SavedJobsActivity extends BaseAuthActivity {

    private JobAdapter adapter;
    private SavedJobsViewModel viewModel;

    private boolean deleteMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.isAdmin()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_saved_jobs);

        RecyclerView rv = findViewById(R.id.rvSavedJobs);
        ImageView btnDelete = findViewById(R.id.btnDelete);
        View btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        rv.setLayoutManager(new LinearLayoutManager(this));

        // ✅ FIXED: 3-ARG CONSTRUCTOR
        adapter = new JobAdapter(
                this,                 // ✅ Context
                new ArrayList<>(),    // ✅ Data
                job -> {
                    if (!deleteMode) {
                        openDetail(job);
                    }
                }
        );


        rv.setAdapter(adapter);

        viewModel = new ViewModelProvider(this)
                .get(SavedJobsViewModel.class);
        viewModel.init(this);

        viewModel.getSavedJobs().observe(this, jobs -> {
            adapter.updateData(jobs);
        });

        // =====================
        // DELETE BUTTON
        // =====================
        btnDelete.setOnClickListener(v -> {

            if (!deleteMode) {
                // ENTER DELETE MODE
                deleteMode = true;
                adapter.enableSelection(true);
                Toast.makeText(
                        this,
                        "Select jobs to delete",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            // CONFIRM DELETE
            var selected = adapter.getSelectedJobIds();

            if (selected.isEmpty()) {
                Toast.makeText(
                        this,
                        "No job selected",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Remove saved jobs?")
                    .setMessage("Selected jobs will be removed.")
                    .setPositiveButton("Delete", (d, w) -> {
                        for (String id : selected) {
                            viewModel.removeSavedJob(id);
                        }
                        exitDeleteMode();
                    })
                    .setNegativeButton("Cancel", (d, w) -> exitDeleteMode())
                    .show();
        });
    }

    private void exitDeleteMode() {
        deleteMode = false;
        adapter.enableSelection(false);
    }

    private void openDetail(Job job) {
        android.content.Intent i =
                new android.content.Intent(
                        this,
                        com.example.find_job.ui.job_detail.JobDetailActivity.class
                );

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
                    new java.util.ArrayList<>(job.requirements)
            );
        }

        startActivity(i);
    }
}
