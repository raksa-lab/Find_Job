package com.example.find_job.ui.job_detail;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.find_job.R;

public class JobDetailActivity extends AppCompatActivity {

    TextView tvTitle, tvCompany, tvLocation, tvSalary, tvEmploymentType, tvDescription;
    Button btnApply, btnViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);   // IMPORTANT!

        // Bind Views
        tvTitle = findViewById(R.id.tvTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalary = findViewById(R.id.tvSalary);
        tvEmploymentType = findViewById(R.id.tvEmploymentType);
        tvDescription = findViewById(R.id.tvDescription);

        btnApply = findViewById(R.id.btnApply);
        btnViewStatus = findViewById(R.id.btnViewStatus);

        // Read Job Data from Intent
        String title = getIntent().getStringExtra("title");
        String company = getIntent().getStringExtra("company");
        String location = getIntent().getStringExtra("location");
        String employmentType = getIntent().getStringExtra("employmentType");
        String description = getIntent().getStringExtra("description");
        int salary = getIntent().getIntExtra("salary", 0);

        // Fill UI
        tvTitle.setText(title);
        tvCompany.setText(company);
        tvLocation.setText(location);
        tvEmploymentType.setText(employmentType);
        tvSalary.setText("$" + salary);
        tvDescription.setText(description);

        btnApply.setOnClickListener(v -> {
            // TODO: open Apply screen
        });
    }
}
