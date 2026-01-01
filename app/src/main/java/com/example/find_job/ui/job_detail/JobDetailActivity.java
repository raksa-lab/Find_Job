package com.example.find_job.ui.job_detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.find_job.MainActivity;
import com.example.find_job.R;
import com.example.find_job.data.repository.ApplicationRepository;
import com.example.find_job.data.repository.FavoriteRepository;
import com.example.find_job.data.repository.UserRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class JobDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvCompany, tvLocation,
            tvSalaryAmount, tvDescription,
            tvRequirements, tvEmploymentType;

    private MaterialButton btnApply, btnViewStatus, btnSave;
    private FloatingActionButton fabShare;

    private String jobId, jobTitle, jobCompany;
    private boolean isSaved = false;

    private FavoriteRepository favoriteRepository;
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        // =========================
        // BACK
        // =========================
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // =========================
        // BIND UI
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

        favoriteRepository = new FavoriteRepository(this);
        applicationRepository = new ApplicationRepository(this);
        userRepository = new UserRepository(this);

        // =========================
        // GET INTENT DATA
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
        // SALARY
        // =========================
        int salary = intent.getIntExtra("salary", 0);
        tvSalaryAmount.setText(
                salary > 0 ? "$" + salary + " / m" : "Negotiable"
        );

        // =========================
        // EMPLOYMENT TYPE
        // =========================
        String employmentType = intent.getStringExtra("employmentType");

        if (employmentType != null && !employmentType.isEmpty()) {
            tvEmploymentType.setText(formatEmploymentType(employmentType));
            tvEmploymentType.setVisibility(View.VISIBLE);
        } else {
            tvEmploymentType.setVisibility(View.GONE);
        }

        // =========================
        // REQUIREMENTS
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
        // FROM APPLIED JOBS
        // =========================
        if (openedFromApplied) {
            btnApply.setVisibility(View.GONE);
            btnViewStatus.setVisibility(View.VISIBLE);
        }

        // =========================
        // ACTIONS
        // =========================
        btnApply.setOnClickListener(v -> checkBeforeApply());

        btnViewStatus.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("open_tab", "application");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });

        btnSave.setOnClickListener(v -> toggleSave());
        fabShare.setOnClickListener(v -> shareJob());
    }

    // =========================
    // APPLY FLOW (CV + DUPLICATE CHECK)
    // =========================
    private void checkBeforeApply() {

        if (jobId == null) {
            Toast.makeText(this, "Invalid job", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1️⃣ Check CV exists
        userRepository.hasResume().observe(this, hasResume -> {
            if (!hasResume) {
                Toast.makeText(
                        this,
                        "Please upload your CV before applying",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }

            // 2️⃣ Check already applied
            applicationRepository.hasApplied(jobId).observe(this, applied -> {
                if (applied) {
                    Toast.makeText(
                            this,
                            "You already applied for this job",
                            Toast.LENGTH_SHORT
                    ).show();

                    btnApply.setEnabled(false);
                    btnApply.setText("Applied");
                    btnViewStatus.setVisibility(View.VISIBLE);
                    return;
                }

                // 3️⃣ Show apply dialog
                showApplyDialog();
            });
        });
    }

    // =========================
    // APPLY DIALOG
    // =========================
    private void showApplyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Apply for Job");

        EditText input = new EditText(this);
        input.setHint("Cover letter (optional)");
        builder.setView(input);

        builder.setPositiveButton("Apply", null);
        builder.setNegativeButton("Cancel", (d, w) -> d.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {

                    String coverLetter = input.getText().toString();

                    applicationRepository.apply(jobId, coverLetter)
                            .observe(this, success -> {

                                if (success) {
                                    Toast.makeText(
                                            this,
                                            "Application submitted",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    btnApply.setEnabled(false);
                                    btnApply.setText("Applied");
                                    btnViewStatus.setVisibility(View.VISIBLE);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(
                                            this,
                                            "Failed to apply. Try again.",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                });
    }

    // =========================
    // SAVE / UNSAVE JOB
    // =========================
    private void toggleSave() {

        if (jobId == null) {
            Toast.makeText(this, "Unable to save job", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isSaved) {
            favoriteRepository.addFavorite(jobId);
            setSavedUI(true);
            Toast.makeText(this, "Saved for later", Toast.LENGTH_SHORT).show();
        } else {
            favoriteRepository.removeFavorite(jobId);
            setSavedUI(false);
            Toast.makeText(this, "Removed from saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void setSavedUI(boolean saved) {
        isSaved = saved;

        if (saved) {
            btnSave.setText("Saved");
            btnSave.setIconResource(R.drawable.ic_bookmark_filled);
            btnSave.setTextColor(getColor(R.color.primary));
        } else {
            btnSave.setText("Save for Later");
            btnSave.setIconResource(R.drawable.ic_bookmark);
            btnSave.setTextColor(getColor(R.color.gray));
        }
    }

    // =========================
    // FORMAT JOB TYPE
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
    // SHARE JOB
    // =========================
    private void shareJob() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(
                Intent.EXTRA_TEXT,
                "Check this job: " + jobTitle + " at " + jobCompany
        );
        startActivity(Intent.createChooser(share, "Share via"));
    }
}
