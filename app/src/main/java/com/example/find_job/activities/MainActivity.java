package com.example.find_job.activities;

import android.os.Bundle;
import android.view.MenuItem; // Correct import order
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.find_job.R;
import com.example.find_job.fragments.ApplicationsFragment;
import com.example.find_job.fragments.FavoritesFragment;
import com.example.find_job.fragments.UserHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Set the default fragment only when the activity is first created
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new UserHomeFragment())
                    .commit();
        }

        // --- THIS IS THE CORRECTED PART ---
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int itemId = item.getItemId(); // Get the ID once for comparison

            if (itemId == R.id.menu_favorites) {
                selectedFragment = new FavoritesFragment();
            } else if (itemId == R.id.menu_apps) {
                selectedFragment = new ApplicationsFragment();
            } else { // This covers R.id.menu_home and any other default case
                selectedFragment = new UserHomeFragment();
            }

            // Replace the current fragment with the selected one
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();

            return true; // Return true to display the item as the selected item
        });
    }
}
