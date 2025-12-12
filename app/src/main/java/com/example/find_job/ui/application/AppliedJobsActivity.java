package com.example.find_job.ui.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.find_job.R;
import com.example.find_job.adapters.AppliedJobAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppliedJobsActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<HashMap<String, String>> appliedList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applied_jobs);

        rv = findViewById(R.id.rvAppliedJobs);
        rv.setLayoutManager(new LinearLayoutManager(this));

        loadAppliedJobs();

        AppliedJobAdapter adapter = new AppliedJobAdapter(this, appliedList);
        rv.setAdapter(adapter);
    }

    private void loadAppliedJobs() {
        SharedPreferences prefs = getSharedPreferences("APPLIED_JOBS", MODE_PRIVATE);

        for (Map.Entry<String, ?> entry : prefs.getAll().entrySet()) {
            String raw = entry.getValue().toString();
            String[] parts = raw.split("\\|\\|");

            // Create map
            HashMap<String, String> map = new HashMap<>();

            // Safe extraction (prevent crash)
            map.put("jobId", parts.length > 0 ? parts[0] : "");
            map.put("title", parts.length > 1 ? parts[1] : "Unknown Title");
            map.put("company", parts.length > 2 ? parts[2] : "Unknown Company");
            map.put("remark", parts.length > 3 ? parts[3] : "No remark");

            appliedList.add(map);
        }
    }


}
