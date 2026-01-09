package com.example.find_job.ui.jobs;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import com.example.find_job.R;
import com.example.find_job.data.models.JobRequest;
import com.example.find_job.data.repository.JobRepository;
import com.example.find_job.ui.base.BaseAdminActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;

public class AddJobActivity extends BaseAdminActivity {

    // =========================
    // UI
    // =========================
    private TextInputEditText etJobTitle;
    private TextInputEditText etDescription;
    private TextInputEditText etCompany;
    private TextInputEditText etLocation;
    private TextInputEditText etSalary;
    private AutoCompleteTextView etJobType;
    private AutoCompleteTextView etExperience;
    private AutoCompleteTextView etCategory;
    private TextInputEditText etRequirements;
    private TextInputEditText etApplicationLink;
    private SwitchMaterial switchRemote;
    private TextInputEditText etSkills;
    private TextInputEditText etBenefits;
    private ImageView ivCompanyLogo;
    private Uri selectedLogoUri;


    private MaterialButton btnSubmitJob;
    private MaterialButton btnSaveDraft;

    // =========================
    // DATA
    // =========================
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

    // =================================================
    // INIT VIEWS
    // =================================================
    private void initViews() {
        etJobTitle = findViewById(R.id.etJobTitle);
        etDescription = findViewById(R.id.etDescription);
        etCompany = findViewById(R.id.etCompany);
        etLocation = findViewById(R.id.etLocation);
        etSalary = findViewById(R.id.etSalary);
        etJobType = findViewById(R.id.etJobType);
        etExperience = findViewById(R.id.etExperience);
        etCategory = findViewById(R.id.etCategory);
        etRequirements = findViewById(R.id.etRequirements);
        etApplicationLink = findViewById(R.id.etApplicationLink);

        switchRemote = findViewById(R.id.switchRemote);
        etSkills = findViewById(R.id.etSkills);
        etBenefits = findViewById(R.id.etBenefits);
        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);


        btnSubmitJob = findViewById(R.id.btnSubmitJob);
        btnSaveDraft = findViewById(R.id.btnSaveDraft);

        findViewById(R.id.btn_back).setOnClickListener(v -> onBackPressed());
    }
    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedLogoUri = uri;
                            ivCompanyLogo.setImageURI(uri);
                        }
                    }
            );

    // =================================================
    // DROPDOWNS
    // =================================================
    private void setupDropdowns() {

        // JOB TYPE
        String[] jobTypes = {
                "Full-Time", "Part-Time", "Contract", "Internship", "Freelance"
        };
        etJobType.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                jobTypes
        ));
        etJobType.setText("Full-Time", false);

        // EXPERIENCE LEVEL
        String[] experienceLevels = {
                "Entry Level", "Junior", "Mid-Level", "Senior", "Lead", "Executive"
        };
        etExperience.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                experienceLevels
        ));
        etExperience.setText("Mid-Level", false);

        // CATEGORY
        String[] categories = {
                "general", "technology", "business",
                "finance", "design", "marketing"
        };
        etCategory.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categories
        ));
        etCategory.setText("general", false);
    }

    // =================================================
    // BUTTONS
    // =================================================
    private void setupClickListeners() {
        btnSubmitJob.setOnClickListener(v -> {
            if (validateForm()) submitJob();
        });
        ivCompanyLogo.setOnClickListener(v ->
                imagePicker.launch("image/*")
        );

        btnSaveDraft.setOnClickListener(v -> saveDraft());
    }

    // =================================================
    // VALIDATION
    // =================================================
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
            Toast.makeText(
                    this,
                    "Please fill in all required fields",
                    Toast.LENGTH_SHORT
            ).show();
        }

        return isValid;
    }


    // =================================================
    // SUBMIT JOB
    // =================================================
    private void submitJob() {

        JobRequest job = new JobRequest();

        // BASIC
        job.title = etJobTitle.getText().toString().trim();
        job.description = etDescription.getText().toString().trim();
        job.company = etCompany.getText().toString().trim();
        job.location = etLocation.getText().toString().trim();
        job.salary = Integer.parseInt(etSalary.getText().toString().trim());

        // TYPES
        job.employmentType = convertJobTypeToBackend(
                etJobType.getText().toString().trim()
        );

        job.experienceLevel = convertExperienceToBackend(
                etExperience.getText().toString().trim()
        );

        job.category = etCategory.getText().toString().trim();
        job.remote = switchRemote.isChecked();
        job.applicationLink = etApplicationLink.getText().toString().trim();

        // REQUIREMENTS
        job.requirements = etRequirements.getText().toString().trim().isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(etRequirements.getText().toString().split("\\n"));

        // SKILLS
        job.skills = etSkills.getText().toString().trim().isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(etSkills.getText().toString().split("\\n"));

        // BENEFITS
        job.benefits = etBenefits.getText().toString().trim().isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(etBenefits.getText().toString().split("\\n"));

        // TAGS
        job.tags = new ArrayList<>();
        job.tags.add(job.employmentType);
        job.tags.add(job.category);
        job.tags.add("hiring");

        job.createdBy = "admin";

        btnSubmitJob.setEnabled(false);
        btnSubmitJob.setText("Posting...");

        // ✅ ONLY THIS
        uploadCompanyLogoAndSubmit(job);
    }
    private void uploadCompanyLogoAndSubmit(JobRequest job) {

        if (selectedLogoUri == null) {
            submitJobToBackend(job);
            return;
        }

        StorageReference storageRef =
                FirebaseStorage.getInstance()
                        .getReference("company_logos")
                        .child(System.currentTimeMillis() + ".jpg");

        storageRef.putFile(selectedLogoUri)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                })
                .addOnSuccessListener(uri -> {
                    job.companyLogo = uri.toString();
                    submitJobToBackend(job);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "Logo upload failed",
                            Toast.LENGTH_LONG
                    ).show();
                    btnSubmitJob.setEnabled(true);
                    btnSubmitJob.setText("Post Job");
                });
    }
    private void submitJobToBackend(JobRequest job) {

        jobRepository.addJob(job, success -> runOnUiThread(() -> {

            btnSubmitJob.setEnabled(false);
            btnSubmitJob.setText("Posting...");



            if (success) {
                Toast.makeText(
                        this,
                        "Job posted successfully!",
                        Toast.LENGTH_SHORT
                ).show();
                clearDraft();
                finish();
            } else {
                Toast.makeText(
                        this,
                        "Failed to post job.",
                        Toast.LENGTH_LONG
                ).show();
            }
        }));
    }

    // =================================================
    // HELPERS
    // =================================================
    private String convertJobTypeToBackend(String jobType) {
        switch (jobType) {
            case "Part-Time": return "part-time";
            case "Contract": return "contract";
            case "Internship": return "internship";
            case "Freelance": return "freelance";
            default: return "full-time";
        }
    }

    private String convertExperienceToBackend(String exp) {
        switch (exp) {
            case "Entry Level": return "entry";
            case "Junior": return "junior";
            case "Mid-Level": return "mid";
            case "Senior": return "senior";
            case "Lead": return "lead";
            case "Executive": return "executive";
            default: return "entry";
        }
    }

    // =================================================
    // DRAFT
    // =================================================
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
                .putString("draft_category", etCategory.getText().toString())
                .putString("draft_requirements", etRequirements.getText().toString())
                .putString("draft_skills", etSkills.getText().toString())
                .putString("draft_benefits", etBenefits.getText().toString())

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
            etCategory.setText(prefs.getString("draft_category", "general"), false);
            etRequirements.setText(prefs.getString("draft_requirements", ""));
            etSkills.setText(prefs.getString("draft_skills", ""));
            etBenefits.setText(prefs.getString("draft_benefits", ""));

        }
    }

    private void clearDraft() {
        getSharedPreferences("job_drafts", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    // =================================================
    // BACK
    // =================================================
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
