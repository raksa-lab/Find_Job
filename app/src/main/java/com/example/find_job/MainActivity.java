package com.example.find_job;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.find_job.ui.job_list.JobListFragment;
import com.example.find_job.ui.home.Home;
import com.example.find_job.ui.profile.ProfileFragment;
import com.example.find_job.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        loadFragment(new Home()); // default page

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment;

            if (item.getItemId() == R.id.menu_home) {
                fragment = new Home();
            } else if (item.getItemId() == R.id.menu_job) {
                fragment = new JobListFragment();
            } else if (item.getItemId() == R.id.menu_search) {
                fragment = new SearchFragment();   // Create later
            } else if (item.getItemId() == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                fragment = new Home();
            }

            loadFragment(fragment);
            return true;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
