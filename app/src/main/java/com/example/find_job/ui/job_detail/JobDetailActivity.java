package com.example.find_job.ui.job_detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.find_job.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class JobDetailActivity extends AppCompatActivity {

    // Declare views
    private TextView tvTitle;
    private TextView tvCompany;
    private TextView tvLocation;
    private TextView tvSalaryAmount;
    private TextView tvDescription;
    private Button btnApply;
    private Button btnViewStatus;
    private Button btnSave;
    private FloatingActionButton fabShare;
    private ImageView ivCompanyLogo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);   // IMPORTANT!

        // Initialize Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(""); // Empty title for collapsing toolbar
        }

        // Bind Views
        tvTitle = findViewById(R.id.tvTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalaryAmount = findViewById(R.id.tvSalaryAmount);
        tvDescription = findViewById(R.id.tvDescription);
        btnApply = findViewById(R.id.btnApply);
        btnViewStatus = findViewById(R.id.btnViewStatus);
        btnSave = findViewById(R.id.btnSave);
        fabShare = findViewById(R.id.fabShare);
        ivCompanyLogo = findViewById(R.id.ivCompanyLogo);

        // Read Job Data from Intent
        String title = getIntent().getStringExtra("title");
        String company = getIntent().getStringExtra("company");
        String location = getIntent().getStringExtra("location");
        String employmentType = getIntent().getStringExtra("employmentType");
        String description = getIntent().getStringExtra("description");
        int salary = getIntent().getIntExtra("salary", 0);

        // Fill UI
        tvTitle.setText(title != null ? title : "Job Title");
        tvCompany.setText(company != null ? company : "Company Name");
        tvLocation.setText(location != null ? location : "Location");

        // Format salary nicely
        if (salary > 0) {
            tvSalaryAmount.setText("$" + formatSalary(salary));
        } else {
            tvSalaryAmount.setText("Negotiable");
        }

        tvDescription.setText(description != null ? description : "No description available");

        // Set click listeners
        btnApply.setOnClickListener(v -> {
            // TODO: open Apply screen
            Toast.makeText(this, "Applying for " + title, Toast.LENGTH_SHORT).show();
            // Add your apply logic here
        });

        btnViewStatus.setOnClickListener(v -> {
            // Handle view status button click
            Toast.makeText(this, "Viewing application status", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to application status screen
        });

        btnSave.setOnClickListener(v -> {
            // Handle save button click
            Toast.makeText(this, "Job saved for later", Toast.LENGTH_SHORT).show();
            // TODO: Save job to database or SharedPreferences
        });

        fabShare.setOnClickListener(v -> {
            // Handle share button click
            shareJob(title, company);
        });
    }

    // Format salary with K suffix for better readability
    private String formatSalary(int salary) {
        if (salary >= 1000) {
            return (salary / 1000) + "k";
        }
        return String.valueOf(salary);
    }

    // Share job function
    private void shareJob(String title, String company) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Check out this job opportunity: " + title + " at " + company;
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Job Opportunity");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    // Handle back button in toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}