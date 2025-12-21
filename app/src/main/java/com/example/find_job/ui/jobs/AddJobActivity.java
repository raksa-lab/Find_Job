package com.example.find_job.ui.jobs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.find_job.R;
import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.repository.JobRepository;
import com.example.find_job.ui.base.BaseAdminActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;

public class AddJobActivity extends BaseAdminActivity {

    private TextInputEditText etJobTitle;
    private TextInputEditText etDescription;
    private TextInputEditText etCompany;
    private TextInputEditText etLocation;
    private TextInputEditText etSalary;
    private AutoCompleteTextView etJobType;
    private AutoCompleteTextView etExperience;
    private TextInputEditText etRequirements;
    private MaterialButton btnSubmitJob;
    private MaterialButton btnSaveDraft;

    private JobRepository jobRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job);

        jobRepository = new JobRepository(this);


        initViews();
        setupDropdowns();
        setupClickListeners();
        loadDraft();
    }

    private void initViews() {
        etJobTitle = findViewById(R.id.etJobTitle);
        etDescription = findViewById(R.id.etDescription);
        etCompany = findViewById(R.id.etCompany);
        etLocation = findViewById(R.id.etLocation);
        etSalary = findViewById(R.id.etSalary);
        etJobType = findViewById(R.id.etJobType);
        etExperience = findViewById(R.id.etExperience);
        etRequirements = findViewById(R.id.etRequirements);
        btnSubmitJob = findViewById(R.id.btnSubmitJob);
        btnSaveDraft = findViewById(R.id.btnSaveDraft);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }

    private void setupDropdowns() {
        String[] jobTypes = {"Full-Time", "Part-Time", "Contract", "Internship", "Freelance"};
        etJobType.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                jobTypes
        ));
        etJobType.setText("Full-Time", false);

        String[] experienceLevels = {
                "Entry Level", "Junior", "Mid-Level", "Senior", "Lead", "Executive"
        };
        etExperience.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                experienceLevels
        ));
        etExperience.setText("Mid-Level", false);
    }

    private void setupClickListeners() {
        btnSubmitJob.setOnClickListener(v -> {
            if (validateForm()) submitJob();
        });

        btnSaveDraft.setOnClickListener(v -> saveDraft());
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (etJobTitle.getText().toString().trim().isEmpty()) {
            etJobTitle.setError("Job title is required");
            etJobTitle.requestFocus();
            isValid = false;
        }

        if (etCompany.getText().toString().trim().isEmpty()) {
            etCompany.setError("Company name is required");
            if (isValid) etCompany.requestFocus();
            isValid = false;
        }

        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Location is required");
            if (isValid) etLocation.requestFocus();
            isValid = false;
        }

        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Job description is required");
            if (isValid) etDescription.requestFocus();
            isValid = false;
        } else if (etDescription.getText().toString().trim().length() < 50) {
            etDescription.setError("Description must be at least 50 characters");
            if (isValid) etDescription.requestFocus();
            isValid = false;
        }

        if (etSalary.getText().toString().trim().isEmpty()) {
            etSalary.setError("Salary is required");
            if (isValid) etSalary.requestFocus();
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private void submitJob() {
        JobRequest job = new JobRequest();

        job.title = etJobTitle.getText().toString().trim();
        job.description = etDescription.getText().toString().trim();
        job.company = etCompany.getText().toString().trim();
        job.location = etLocation.getText().toString().trim();
        job.salary = Integer.parseInt(etSalary.getText().toString().trim());

        job.requirements = etRequirements.getText().toString().trim().isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(etRequirements.getText().toString().split("\\n"));

        job.employmentType = convertJobTypeToBackend(
                etJobType.getText().toString().trim()
        );

        job.tags = Arrays.asList(job.employmentType, "hiring");

        job.createdBy = "admin"; // should come from SessionManager

        btnSubmitJob.setEnabled(false);
        btnSubmitJob.setText("Posting...");

        jobRepository.addJob(job, success -> runOnUiThread(() -> {
            btnSubmitJob.setEnabled(true);
            btnSubmitJob.setText("Post Job");

            if (success) {
                Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                clearDraft();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Failed to post job.", Toast.LENGTH_LONG).show();
            }
        }));
    }

    private String convertJobTypeToBackend(String jobType) {
        switch (jobType) {
            case "Part-Time": return "part-time";
            case "Contract": return "contract";
            case "Internship": return "internship";
            case "Freelance": return "freelance";
            default: return "full-time";
        }
    }

    private void saveDraft() {
        SharedPreferences prefs = getSharedPreferences("job_drafts", MODE_PRIVATE);
        prefs.edit()
                .putString("draft_title", etJobTitle.getText().toString())
                .putString("draft_description", etDescription.getText().toString())
                .putString("draft_company", etCompany.getText().toString())
                .putString("draft_location", etLocation.getText().toString())
                .putString("draft_salary", etSalary.getText().toString())
                .putString("draft_job_type", etJobType.getText().toString())
                .putString("draft_experience", etExperience.getText().toString())
                .putString("draft_requirements", etRequirements.getText().toString())
                .apply();

        Toast.makeText(this, "Draft saved", Toast.LENGTH_SHORT).show();
    }

    private void loadDraft() {
        SharedPreferences prefs = getSharedPreferences("job_drafts", MODE_PRIVATE);
        if (!prefs.getString("draft_title", "").isEmpty()) {
            etJobTitle.setText(prefs.getString("draft_title", ""));
            etDescription.setText(prefs.getString("draft_description", ""));
            etCompany.setText(prefs.getString("draft_company", ""));
            etLocation.setText(prefs.getString("draft_location", ""));
            etSalary.setText(prefs.getString("draft_salary", ""));
            etJobType.setText(prefs.getString("draft_job_type", "Full-Time"), false);
            etExperience.setText(prefs.getString("draft_experience", "Mid-Level"), false);
            etRequirements.setText(prefs.getString("draft_requirements", ""));
        }
    }

    private void clearDraft() {
        getSharedPreferences("job_drafts", MODE_PRIVATE).edit().clear().apply();
    }

    @Override
    public void onBackPressed() {
        if (hasUnsavedChanges()) {
            new AlertDialog.Builder(this)
                    .setTitle("Discard Changes?")
                    .setMessage("Save draft before leaving?")
                    .setPositiveButton("Discard", (d, w) -> {
                        clearDraft();
                        super.onBackPressed();
                    })
                    .setNegativeButton("Save Draft", (d, w) -> {
                        saveDraft();
                        super.onBackPressed();
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasUnsavedChanges() {
        return !etJobTitle.getText().toString().isEmpty()
                || !etDescription.getText().toString().isEmpty();
    }
}
