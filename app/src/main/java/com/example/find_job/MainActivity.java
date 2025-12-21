package com.example.find_job;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.ui.application.AppliedJobsFragment;
import com.example.find_job.ui.home.Home;
import com.example.find_job.ui.job_list.JobListFragment;
import com.example.find_job.ui.profile.ProfileFragment;
import com.example.find_job.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        // 🔐 MUST BE LOGGED IN
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupBottomNavigationByRole();

        loadFragment(new Home());

        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.menu_home) {
                loadFragment(new Home());
                return true;

            } else if (id == R.id.menu_job) {
                loadFragment(new JobListFragment());
                return true;

            } else if (id == R.id.menu_application) {

                // 🚫 ADMIN CANNOT ACCESS APPLICATIONS
                if (sessionManager.isAdmin()) {
                    return false;
                }

                loadFragment(new AppliedJobsFragment());
                return true;

            } else if (id == R.id.appBarLayout) {
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

    // ===============================
    // ROLE-BASED BOTTOM NAV
    // ===============================
    private void setupBottomNavigationByRole() {

        if (sessionManager.isAdmin()) {
            // Remove Job Seeker–only tab
            bottomNavigationView.getMenu()
                    .removeItem(R.id.menu_application);
        }
    }

    // ===============================
    // DEEP LINK HANDLING
    // ===============================
    private void handleNavigationIntent(Intent intent) {
        if (intent == null) return;

        String tab = intent.getStringExtra("open_tab");
        if (tab == null) return;

        if (tab.equals("application") && !sessionManager.isAdmin()) {
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

    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
