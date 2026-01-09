package com.example.find_job.ui.job_detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.find_job.MainActivity;
import com.example.find_job.R;
import com.example.find_job.data.repository.FavoriteRepository;
import com.example.find_job.ui.application.ApplyJobViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class JobDetailActivity extends AppCompatActivity {

    // ================= UI =================
    private ImageView ivCompanyLogo;

    private TextView tvTitle, tvCompany, tvLocation;
    private TextView tvSalaryAmount, tvDescription;
    private TextView tvRequirements, tvPostedDate, tvApplicants;

    private TextView tvSkillsTitle, tvSkills;
    private TextView tvBenefitsTitle, tvBenefits;

    private Chip chipWorkType, chipEmploymentType, chipExperience;

    private MaterialButton btnApply, btnViewStatus, btnSave;
    private FloatingActionButton fabShare;

    // ================= DATA =================
    private String jobId, jobTitle, jobCompany;
    private boolean isSaved = false;
    private boolean isApplied = false;

    private FavoriteRepository favoriteRepository;
    private ApplyJobViewModel applyJobViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // ================= BIND UI =================
        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);

        tvTitle = findViewById(R.id.tvTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalaryAmount = findViewById(R.id.tvSalaryAmount);
        tvDescription = findViewById(R.id.tvDescription);
        tvRequirements = findViewById(R.id.tvRequirements);
        tvPostedDate = findViewById(R.id.tvPostedDate);
        tvApplicants = findViewById(R.id.tvApplicants);

        tvSkillsTitle = findViewById(R.id.tvSkillsTitle);
        tvSkills = findViewById(R.id.tvSkills);
        tvBenefitsTitle = findViewById(R.id.tvBenefitsTitle);
        tvBenefits = findViewById(R.id.tvBenefits);

        chipWorkType = findViewById(R.id.chipWorkType);
        chipEmploymentType = findViewById(R.id.chipEmploymentType);
        chipExperience = findViewById(R.id.chipExperience);

        btnApply = findViewById(R.id.btnApply);
        btnViewStatus = findViewById(R.id.btnViewStatus);
        btnSave = findViewById(R.id.btnSave);
        fabShare = findViewById(R.id.fabShare);

        favoriteRepository = new FavoriteRepository(this);
        applyJobViewModel = new ViewModelProvider(this).get(ApplyJobViewModel.class);

        // ================= GET DATA =================
        Intent intent = getIntent();

        jobId = intent.getStringExtra("jobId");
        jobTitle = intent.getStringExtra("title");
        jobCompany = intent.getStringExtra("company");

        tvTitle.setText(jobTitle != null ? jobTitle : "—");
        tvCompany.setText(jobCompany != null ? jobCompany : "—");
        tvLocation.setText(intent.getStringExtra("location"));
        tvDescription.setText(intent.getStringExtra("description"));

        int salary = intent.getIntExtra("salary", 0);
        tvSalaryAmount.setText(salary > 0 ? "$" + salary + " / m" : "Negotiable");

        // ================= COMPANY LOGO =================
        String logoUrl = intent.getStringExtra("companyLogo");
        Glide.with(this)
                .load(logoUrl)
                .placeholder(R.drawable.ic_company_placeholder)
                .error(R.drawable.ic_company_placeholder)
                .into(ivCompanyLogo);

        // ================= CHIPS =================
        chipExperience.setText(formatText(intent.getStringExtra("experienceLevel"), "—"));

        boolean remote = intent.getBooleanExtra("remote", false);
        chipWorkType.setText(remote ? "Remote" : "On-site");

        chipEmploymentType.setText(formatEmploymentType(intent.getStringExtra("employmentType")));

        // ================= APPLICANTS =================
        tvApplicants.setText(String.valueOf(intent.getIntExtra("applicantsCount", 0)));

        // ================= POSTED DATE =================
        long createdSeconds = intent.getLongExtra("createdAtSeconds", 0);
        if (createdSeconds > 0) {
            long createdMillis = createdSeconds * 1000;
            CharSequence timeAgo =
                    android.text.format.DateUtils.getRelativeTimeSpanString(
                            createdMillis,
                            System.currentTimeMillis(),
                            android.text.format.DateUtils.DAY_IN_MILLIS
                    );
            tvPostedDate.setText(timeAgo);
        } else {
            tvPostedDate.setText("—");
        }

        // ================= REQUIREMENTS =================
        ArrayList<String> requirements = intent.getStringArrayListExtra("requirements");
        tvRequirements.setText(
                requirements != null && !requirements.isEmpty()
                        ? toBulletText(requirements)
                        : "No requirements listed."
        );

        // ================= SKILLS =================
        ArrayList<String> skills = intent.getStringArrayListExtra("skills");
        if (skills != null && !skills.isEmpty()) {
            tvSkillsTitle.setVisibility(View.VISIBLE);
            tvSkills.setVisibility(View.VISIBLE);
            tvSkills.setText(toBulletText(skills));
        } else {
            tvSkillsTitle.setVisibility(View.GONE);
            tvSkills.setVisibility(View.GONE);
        }

        // ================= BENEFITS =================
        ArrayList<String> benefits = intent.getStringArrayListExtra("benefits");
        if (benefits != null && !benefits.isEmpty()) {
            tvBenefitsTitle.setVisibility(View.VISIBLE);
            tvBenefits.setVisibility(View.VISIBLE);
            tvBenefits.setText(toBulletText(benefits));
        } else {
            tvBenefitsTitle.setVisibility(View.GONE);
            tvBenefits.setVisibility(View.GONE);
        }

        // ================= ACTIONS =================
        btnApply.setOnClickListener(v -> startApplyFlow());
        btnSave.setOnClickListener(v -> toggleSave());
        fabShare.setOnClickListener(v -> shareJob());

        btnViewStatus.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("open_tab", "application");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });

        syncInitialState();
    }

    // ================= STATE =================
    private void syncInitialState() {
        if (jobId == null) return;

        applyJobViewModel.hasApplied(jobId)
                .observe(this, applied -> {
                    isApplied = Boolean.TRUE.equals(applied);
                    updateApplyUI();
                });

        favoriteRepository.isFavoriteLive(jobId)
                .observe(this, this::setSavedUI);
    }

    private void updateApplyUI() {
        if (isApplied) {
            btnApply.setEnabled(false);
            btnApply.setText("Applied");
            btnViewStatus.setVisibility(View.VISIBLE);
        } else {
            btnApply.setEnabled(true);
            btnApply.setText("Apply Now");
            btnViewStatus.setVisibility(View.GONE);
        }
    }

    // ================= APPLY =================
    private void startApplyFlow() {
        if (isApplied) return;

        applyJobViewModel.hasResume().observe(this, hasResume -> {
            if (!Boolean.TRUE.equals(hasResume)) {
                Toast.makeText(this,
                        "Please upload your CV first",
                        Toast.LENGTH_LONG).show();
                return;
            }
            showApplyDialog();
        });
    }

    private void showApplyDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 0);

        EditText etCover = new EditText(this);
        etCover.setHint("Cover letter (optional)");
        layout.addView(etCover);

        new AlertDialog.Builder(this)
                .setTitle("Apply for Job")
                .setView(layout)
                .setPositiveButton("Apply", (d, w) ->
                        applyJobViewModel
                                .apply(jobId, etCover.getText().toString())
                                .observe(this, success -> {
                                    if (Boolean.TRUE.equals(success)) {
                                        isApplied = true;
                                        updateApplyUI();
                                    }
                                })
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ================= SAVE =================
    private void toggleSave() {
        if (isSaved) {
            favoriteRepository.removeFavorite(jobId);
            setSavedUI(false);
        } else {
            favoriteRepository.addFavorite(jobId);
            setSavedUI(true);
        }
    }

    private void setSavedUI(boolean saved) {
        isSaved = saved;
        btnSave.setText(saved ? "Saved" : "Save");
        btnSave.setIconResource(
                saved ? R.drawable.ic_bookmark_filled : R.drawable.ic_bookmark
        );
    }

    // ================= HELPERS =================
    private String toBulletText(ArrayList<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append("• ").append(s).append("\n");
        }
        return sb.toString();
    }

    private String formatEmploymentType(String type) {
        if (type == null) return "—";
        switch (type.toLowerCase()) {
            case "full-time": return "Full-time";
            case "part-time": return "Part-time";
            case "internship": return "Internship";
            default: return type;
        }
    }

    private String formatText(String value, String fallback) {
        return TextUtils.isEmpty(value) ? fallback : value;
    }

    private void shareJob() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,
                "Check this job: " + jobTitle + " at " + jobCompany);
        startActivity(Intent.createChooser(share, "Share job"));
    }
}
