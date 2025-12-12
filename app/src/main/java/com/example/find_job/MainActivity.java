package com.example.find_job;

import android.annotation.SuppressLint;
import android.content.Intent;
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

            if (item.getItemId() == R.id.menu_home) {
                loadFragment(new Home());
                return true;

            } else if (item.getItemId() == R.id.menu_job) {
                loadFragment(new JobListFragment());
                return true;

            } else if (item.getItemId() == R.id.menu_application) {
                // OPEN APPLICATION SCREEN
                startActivity(new Intent(MainActivity.this, com.example.find_job.ui.application.AppliedJobsActivity.class));
                return true;

            }
//            else if (item.getItemId() == R.id.menu_search) {
//                loadFragment(new SearchFragment());
//                return true;
//            }
            else if (item.getItemId() == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }

            return false;
        });



    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
