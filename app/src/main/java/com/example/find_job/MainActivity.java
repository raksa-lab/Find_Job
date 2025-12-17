package com.example.find_job;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.find_job.ui.application.AppliedJobsFragment;
import com.example.find_job.ui.home.Home;
import com.example.find_job.ui.job_list.JobListFragment;
import com.example.find_job.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(new Home());

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.menu_home) {
                loadFragment(new Home());
                return true;

            } else if (item.getItemId() == R.id.menu_job) {
                loadFragment(new JobListFragment());
                return true;

            } else if (item.getItemId() == R.id.menu_application) {
                loadFragment(new AppliedJobsFragment());
                return true;

            } else if (item.getItemId() == R.id.appBarLayout) {
                loadFragment(new ProfileFragment());
                return true;
            }

            return false;
        });

        handleNavigationIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNavigationIntent(intent);
    }

    private void handleNavigationIntent(Intent intent) {
        if (intent == null) return;

        String tab = intent.getStringExtra("open_tab");
        if (tab == null) return;

        if (tab.equals("application")) {
            bottomNavigationView.setSelectedItemId(R.id.menu_application);
            loadFragment(new AppliedJobsFragment());

        } else if (tab.equals("job")) {
            bottomNavigationView.setSelectedItemId(R.id.menu_job);
            loadFragment(new JobListFragment());
        }
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
