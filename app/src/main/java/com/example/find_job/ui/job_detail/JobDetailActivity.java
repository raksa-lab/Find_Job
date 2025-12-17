package com.example.find_job.ui.job_detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.find_job.MainActivity;
import com.example.find_job.R;
import com.example.find_job.ui.saved.SavedJobsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class JobDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvCompany, tvLocation,
            tvSalaryAmount, tvDescription,
            tvRequirements, tvEmploymentType;

    private Button btnApply, btnViewStatus, btnSave;
    private FloatingActionButton fabShare;

    private String jobId, jobTitle, jobCompany;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // Back
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // =========================
        // Bind UI
        // =========================
        tvTitle = findViewById(R.id.tvTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalaryAmount = findViewById(R.id.tvSalaryAmount);
        tvDescription = findViewById(R.id.tvDescription);
        tvRequirements = findViewById(R.id.tvRequirements);
        tvEmploymentType = findViewById(R.id.tvEmploymentType);

        btnApply = findViewById(R.id.btnApply);
        btnViewStatus = findViewById(R.id.btnViewStatus);
        btnSave = findViewById(R.id.btnSave);
        fabShare = findViewById(R.id.fabShare);

        // =========================
        // Get Intent Data
        // =========================
        Intent intent = getIntent();
        boolean openedFromApplied =
                intent.getBooleanExtra("fromApplied", false);

        jobId = intent.getStringExtra("jobId");
        jobTitle = intent.getStringExtra("title");
        jobCompany = intent.getStringExtra("company");

        tvTitle.setText(jobTitle != null ? jobTitle : "—");
        tvCompany.setText(jobCompany != null ? jobCompany : "—");
        tvLocation.setText(
                intent.getStringExtra("location") != null
                        ? intent.getStringExtra("location")
                        : "—"
        );

        tvDescription.setText(
                intent.getStringExtra("description") != null
                        ? intent.getStringExtra("description")
                        : "No description available."
        );

        // =========================
        // Salary
        // =========================
        int salary = intent.getIntExtra("salary", 0);
        tvSalaryAmount.setText(
                salary > 0 ? "$" + salary + " / m" : "Negotiable"
        );

        // =========================
        // Employment Type
        // =========================
        String employmentType =
                intent.getStringExtra("employmentType");

        if (employmentType != null && !employmentType.isEmpty()) {
            tvEmploymentType.setText(
                    formatEmploymentType(employmentType)
            );
            tvEmploymentType.setVisibility(View.VISIBLE);
        } else {
            tvEmploymentType.setVisibility(View.GONE);
        }

        // =========================
        // Requirements
        // =========================
        ArrayList<String> reqList =
                intent.getStringArrayListExtra("requirements");

        if (reqList == null || reqList.isEmpty()) {
            tvRequirements.setText("No requirements listed.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String r : reqList) {
                sb.append("• ").append(r).append("\n");
            }
            tvRequirements.setText(sb.toString());
        }

        // =========================
        // From Applied Jobs
        // =========================
        if (openedFromApplied) {
            btnApply.setVisibility(View.GONE);
            btnViewStatus.setVisibility(View.GONE);
        }

        // =========================
        // ACTIONS
        // =========================
        btnApply.setOnClickListener(v -> showApplyDialog());

        btnViewStatus.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("open_tab", "application");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });

        btnSave.setOnClickListener(v -> saveJobForLater());

        fabShare.setOnClickListener(v -> shareJob());
    }

    // =========================
    // SAVE JOB (NEW)
    // =========================
    private void saveJobForLater() {

        if (jobId == null) {
            Toast.makeText(this, "Unable to save job", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs =
                getSharedPreferences("saved_jobs", MODE_PRIVATE);

        Set<String> saved =
                prefs.getStringSet("job_ids", new HashSet<>());

        Set<String> updated = new HashSet<>(saved);

        if (updated.contains(jobId)) {
            Toast.makeText(this, "Job already saved", Toast.LENGTH_SHORT).show();

            // ✅ Route even if already saved
            startActivity(new Intent(this, SavedJobsActivity.class));
            return;
        }

        updated.add(jobId);
        prefs.edit().putStringSet("job_ids", updated).apply();

        Toast.makeText(this, "Saved for later", Toast.LENGTH_SHORT).show();

        // ✅ ROUTE TO SAVED JOBS PAGE
        startActivity(new Intent(this, SavedJobsActivity.class));
    }


    // =========================
    // Format Job Type
    // =========================
    private String formatEmploymentType(String type) {
        switch (type.toLowerCase()) {
            case "full-time":
            case "full_time":
                return "Full-Time";
            case "part-time":
            case "part_time":
                return "Part-Time";
            case "intern":
            case "internship":
                return "Intern";
            default:
                return type;
        }
    }

    // =========================
    // Apply Dialog
    // =========================
    private void showApplyDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setTitle("Apply for Job");

        EditText remarkInput = new EditText(this);
        remarkInput.setHint("Add remark (optional)");
        builder.setView(remarkInput);

        builder.setPositiveButton("Submit",
                (d, w) -> saveApplication(
                        remarkInput.getText().toString()
                ));

        builder.setNegativeButton("Cancel",
                (d, w) -> d.dismiss());

        builder.show();
    }

    private void saveApplication(String remark) {
        SharedPreferences prefs =
                getSharedPreferences("APPLIED_JOBS", MODE_PRIVATE);

        String record =
                jobId + "||" + jobTitle + "||" + jobCompany + "||" + remark;

        prefs.edit().putString(jobId, record).apply();

        Toast.makeText(this,
                "Application Submitted!",
                Toast.LENGTH_SHORT).show();
    }

    // =========================
    // Share Job
    // =========================
    private void shareJob() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,
                "Check this job: " + jobTitle + " at " + jobCompany);
        startActivity(Intent.createChooser(share, "Share via"));
    }
}
