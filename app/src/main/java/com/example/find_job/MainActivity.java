package com.example.find_job;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.ui.admin.AdminApplicationsFragment;
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

        // ===============================
        // LOGIN CHECK
        // ===============================
        if (!sessionManager.isLoggedIn()) {
            redirectToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // ===============================
        // ROLE-BASED MENU
        // ===============================
        setupBottomNavigationByRole();

        // ===============================
        // HANDLE INITIAL INTENT ROUTING
        // ===============================
        handleIntentRouting(getIntent());

        // ===============================
        // BOTTOM NAVIGATION LISTENER
        // ===============================
        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            if (sessionManager.isAdmin()) {

                // ===== ADMIN NAV =====
                if (item.getItemId() == R.id.menu_admin_jobs) {
                    fragment = new JobListFragment();

                } else if (item.getItemId() == R.id.menu_admin_applications) {
                    fragment = new AdminApplicationsFragment();

                } else if (item.getItemId() == R.id.menu_admin_profile) {
                    fragment = new ProfileFragment();
                }

            } else {

                // ===== USER NAV =====
                if (item.getItemId() == R.id.menu_home) {
                    fragment = new Home();

                } else if (item.getItemId() == R.id.menu_job) {
                    fragment = new JobListFragment();

                } else if (item.getItemId() == R.id.menu_application) {
                    fragment = new AppliedJobsFragment();

                } else if (item.getItemId() == R.id.menu_profile) {
                    fragment = new ProfileFragment();
                }
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }

            return false;
        });
    }

    // ===============================
    // HANDLE NEW INTENT (IMPORTANT FIX)
    // ===============================
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntentRouting(intent);
    }

    // ===============================
    // INTENT ROUTING LOGIC
    // ===============================
    private void handleIntentRouting(Intent intent) {

        String openTab = intent.getStringExtra("open_tab");

        if (openTab != null && !sessionManager.isAdmin()) {

            switch (openTab) {
                case "application":
                    bottomNavigationView.setSelectedItemId(R.id.menu_application);
                    loadFragment(new AppliedJobsFragment());
                    break;

                case "job":
                    bottomNavigationView.setSelectedItemId(R.id.menu_job);
                    loadFragment(new JobListFragment());
                    break;

                case "profile":
                    bottomNavigationView.setSelectedItemId(R.id.menu_profile);
                    loadFragment(new ProfileFragment());
                    break;

                default:
                    bottomNavigationView.setSelectedItemId(R.id.menu_home);
                    loadFragment(new Home());
                    break;
            }

        } else {
            // ===============================
            // DEFAULT APP LAUNCH
            // ===============================
            if (sessionManager.isAdmin()) {
                bottomNavigationView.setSelectedItemId(R.id.menu_admin_jobs);
                loadFragment(new JobListFragment());
            } else {
                bottomNavigationView.setSelectedItemId(R.id.menu_home);
                loadFragment(new Home());
            }
        }
    }

    // ===============================
    // ROLE-BASED MENU SETUP
    // ===============================
    private void setupBottomNavigationByRole() {
        bottomNavigationView.getMenu().clear();

        if (sessionManager.isAdmin()) {
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_admin);
        } else {
            bottomNavigationView.inflateMenu(R.menu.menu_bottom_user);
        }
    }

    // ===============================
    // OPEN PROFILE TAB (OPTIONAL USE)
    // ===============================
    public void openProfileTab() {
        if (sessionManager.isAdmin()) {
            bottomNavigationView.setSelectedItemId(R.id.menu_admin_profile);
            loadFragment(new ProfileFragment());
        } else {
            bottomNavigationView.setSelectedItemId(R.id.menu_profile);
            loadFragment(new ProfileFragment());
        }
    }

    // ===============================
    // FRAGMENT LOADER
    // ===============================
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    // ===============================
    // LOGIN REDIRECT
    // ===============================
    private void redirectToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
