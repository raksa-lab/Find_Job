package com.example.find_job.ui.jobs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.find_job.R;
import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.repository.JobRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.Arrays;

public class AddJobActivity extends AppCompatActivity {

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

        jobRepository = new JobRepository();

        // Initialize views
        initViews();

        // Setup dropdowns
        setupDropdowns();

        // Setup click listeners
        setupClickListeners();

        // Load draft if exists
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

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }

    private void setupDropdowns() {
        // Job Type Dropdown
        String[] jobTypes = {"Full-Time", "Part-Time", "Contract", "Internship", "Freelance"};
        ArrayAdapter<String> jobTypeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                jobTypes
        );
        etJobType.setAdapter(jobTypeAdapter);
        etJobType.setText("Full-Time", false); // Set default

        // Experience Level Dropdown
        String[] experienceLevels = {"Entry Level", "Junior", "Mid-Level", "Senior", "Lead", "Executive"};
        ArrayAdapter<String> experienceAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                experienceLevels
        );
        etExperience.setAdapter(experienceAdapter);
        etExperience.setText("Mid-Level", false); // Set default
    }

    private void setupClickListeners() {
        // Submit Job Button
        btnSubmitJob.setOnClickListener(v -> {
            if (validateForm()) {
                submitJob();
            }
        });

        // Save Draft Button
        btnSaveDraft.setOnClickListener(v -> {
            saveDraft();
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate Job Title
        if (etJobTitle.getText().toString().trim().isEmpty()) {
            etJobTitle.setError("Job title is required");
            etJobTitle.requestFocus();
            isValid = false;
        }

        // Validate Company
        if (etCompany.getText().toString().trim().isEmpty()) {
            etCompany.setError("Company name is required");
            if (isValid) etCompany.requestFocus();
            isValid = false;
        }

        // Validate Location
        if (etLocation.getText().toString().trim().isEmpty()) {
            etLocation.setError("Location is required");
            if (isValid) etLocation.requestFocus();
            isValid = false;
        }

        // Validate Description
        if (etDescription.getText().toString().trim().isEmpty()) {
            etDescription.setError("Job description is required");
            if (isValid) etDescription.requestFocus();
            isValid = false;
        } else if (etDescription.getText().toString().trim().length() < 50) {
            etDescription.setError("Description must be at least 50 characters");
            if (isValid) etDescription.requestFocus();
            isValid = false;
        }

        // Validate Salary
        if (etSalary.getText().toString().trim().isEmpty()) {
            etSalary.setError("Salary is required");
            if (isValid) etSalary.requestFocus();
            isValid = false;
        }

        // Validate Job Type
        if (etJobType.getText().toString().trim().isEmpty()) {
            etJobType.setError("Job type is required");
            if (isValid) etJobType.requestFocus();
            isValid = false;
        }

        // Validate Experience Level
        if (etExperience.getText().toString().trim().isEmpty()) {
            etExperience.setError("Experience level is required");
            if (isValid) etExperience.requestFocus();
            isValid = false;
        }

        // Validate Requirements
        if (etRequirements.getText().toString().trim().isEmpty()) {
            etRequirements.setError("Requirements are required");
            if (isValid) etRequirements.requestFocus();
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
        }

        return isValid;
    }

    private void submitJob() {
        String title = etJobTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String company = etCompany.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String salaryText = etSalary.getText().toString().trim();
        String requirementsText = etRequirements.getText().toString().trim();
        String jobType = etJobType.getText().toString().trim();

        int salary;
        try {
            salary = Integer.parseInt(salaryText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Salary must be a number", Toast.LENGTH_SHORT).show();
            etSalary.setError("Invalid salary");
            etSalary.requestFocus();
            return;
        }

        // Create job request
        JobRequest job = new JobRequest();
        job.title = title;
        job.description = description;
        job.company = company;
        job.location = location;
        job.salary = salary;

        // Parse requirements (split by newline)
        job.requirements = requirementsText.isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(requirementsText.split("\\n"));

        // Convert job type to backend format
        job.employmentType = convertJobTypeToBackend(jobType);

        // Add tags based on experience level or job type (customize as needed)
        job.tags = Arrays.asList(job.employmentType, "hiring");

        // REQUIRED by backend
        job.createdBy = "user_demo_20"; // TODO: Replace with actual user ID

        // Show loading
        btnSubmitJob.setEnabled(false);
        btnSubmitJob.setText("Posting...");

        jobRepository.addJob(job, success -> {

            runOnUiThread(() -> {
                btnSubmitJob.setEnabled(true);
                btnSubmitJob.setText("Post Job");

                if (success) {
                    Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                    clearDraft(); // Clear saved draft
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to post job. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private String convertJobTypeToBackend(String jobType) {
        switch (jobType) {
            case "Full-Time":
                return "full-time";
            case "Part-Time":
                return "part-time";
            case "Contract":
                return "contract";
            case "Internship":
                return "internship";
            case "Freelance":
                return "freelance";
            default:
                return "full-time";
        }
    }

    private void saveDraft() {
        SharedPreferences prefs = getSharedPreferences("job_drafts", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("draft_title", etJobTitle.getText().toString().trim());
        editor.putString("draft_description", etDescription.getText().toString().trim());
        editor.putString("draft_company", etCompany.getText().toString().trim());
        editor.putString("draft_location", etLocation.getText().toString().trim());
        editor.putString("draft_salary", etSalary.getText().toString().trim());
        editor.putString("draft_job_type", etJobType.getText().toString().trim());
        editor.putString("draft_experience", etExperience.getText().toString().trim());
        editor.putString("draft_requirements", etRequirements.getText().toString().trim());

        editor.apply();

        Toast.makeText(this, "Draft saved successfully!", Toast.LENGTH_SHORT).show();
    }

    private void loadDraft() {
        SharedPreferences prefs = getSharedPreferences("job_drafts", MODE_PRIVATE);

        String title = prefs.getString("draft_title", "");
        if (!title.isEmpty()) {
            etJobTitle.setText(title);
            etDescription.setText(prefs.getString("draft_description", ""));
            etCompany.setText(prefs.getString("draft_company", ""));
            etLocation.setText(prefs.getString("draft_location", ""));
            etSalary.setText(prefs.getString("draft_salary", ""));
            etJobType.setText(prefs.getString("draft_job_type", "Full-Time"), false);
            etExperience.setText(prefs.getString("draft_experience", "Mid-Level"), false);
            etRequirements.setText(prefs.getString("draft_requirements", ""));

            Toast.makeText(this, "Draft loaded", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearDraft() {
        SharedPreferences prefs = getSharedPreferences("job_drafts", MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    @Override
    public void onBackPressed() {
        // Check if form has unsaved changes
        if (hasUnsavedChanges()) {
            // Show confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Discard Changes?")
                    .setMessage("You have unsaved changes. Do you want to save them as a draft?")
                    .setPositiveButton("Discard", (dialog, which) -> {
                        clearDraft();
                        super.onBackPressed();
                    })
                    .setNegativeButton("Save Draft", (dialog, which) -> {
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
        return !etJobTitle.getText().toString().trim().isEmpty() ||
                !etDescription.getText().toString().trim().isEmpty() ||
                !etCompany.getText().toString().trim().isEmpty() ||
                !etLocation.getText().toString().trim().isEmpty() ||
                !etSalary.getText().toString().trim().isEmpty() ||
                !etRequirements.getText().toString().trim().isEmpty();
    }
}