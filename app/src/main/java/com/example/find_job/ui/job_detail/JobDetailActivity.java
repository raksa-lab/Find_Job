package com.example.find_job.ui.job_detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.find_job.R;
import com.example.find_job.ui.application.AppliedJobsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class JobDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvCompany, tvLocation, tvSalaryAmount, tvDescription, tvRequirements;
    private Button btnApply, btnViewStatus, btnSave;
    private FloatingActionButton fabShare;

    private String jobId, jobTitle, jobCompany;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // Toolbar Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // IMPORTANT — hide title "findjob"
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Bind UI
        tvTitle = findViewById(R.id.tvTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalaryAmount = findViewById(R.id.tvSalaryAmount);
        tvDescription = findViewById(R.id.tvDescription);
        tvRequirements = findViewById(R.id.tvRequirements);

        btnApply = findViewById(R.id.btnApply);
        btnViewStatus = findViewById(R.id.btnViewStatus);
        btnSave = findViewById(R.id.btnSave);
        fabShare = findViewById(R.id.fabShare);

        // Receive job data
        Intent intent = getIntent();
        boolean openedFromApplied = intent.getBooleanExtra("fromApplied", false);

        jobId = intent.getStringExtra("jobId");
        jobTitle = intent.getStringExtra("title");
        jobCompany = intent.getStringExtra("company");

        tvTitle.setText(jobTitle);
        tvCompany.setText(jobCompany);
        tvLocation.setText(intent.getStringExtra("location"));
        tvDescription.setText(intent.getStringExtra("description"));

        int salary = intent.getIntExtra("salary", 0);
        tvSalaryAmount.setText(salary > 0 ? "$" + salary : "Negotiable");

        // Requirements
        ArrayList<String> reqList = intent.getStringArrayListExtra("requirements");
        if (reqList == null || reqList.isEmpty()) {
            tvRequirements.setText("No requirements listed.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String r : reqList) sb.append("• ").append(r).append("\n");
            tvRequirements.setText(sb.toString());
        }

        // Hide Apply buttons if this job is already applied
        if (openedFromApplied) {
            btnApply.setVisibility(View.GONE);
            btnViewStatus.setVisibility(View.GONE);
        }

        // Apply Button
        btnApply.setOnClickListener(v -> showApplyDialog());

        // View Application Status
        btnViewStatus.setOnClickListener(v ->
                startActivity(new Intent(this, AppliedJobsActivity.class))
        );

        // Share Button
        fabShare.setOnClickListener(v -> shareJob());
    }

    // Popup Dialog for applying
    private void showApplyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Apply for Job");

        final EditText remarkInput = new EditText(this);
        remarkInput.setHint("Add remark (optional)");
        builder.setView(remarkInput);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String remark = remarkInput.getText().toString();
            saveApplication(remark);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void saveApplication(String remark) {
        SharedPreferences prefs = getSharedPreferences("APPLIED_JOBS", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String record = jobId + "||" + jobTitle + "||" + jobCompany + "||" + remark;
        editor.putString(jobId, record);
        editor.apply();

        Toast.makeText(this, "Application Submitted!", Toast.LENGTH_SHORT).show();
    }

    private void shareJob() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "Check this job: " + jobTitle + " at " + jobCompany);
        startActivity(Intent.createChooser(share, "Share via"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
