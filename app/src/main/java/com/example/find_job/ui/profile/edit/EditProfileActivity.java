package com.example.find_job.ui.profile.edit;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.find_job.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends AppCompatActivity {

    // ===== UI =====
    private TextInputEditText etName, etPosition, etLocation, etAbout, etSkill;
    private ChipGroup chipGroupSkills;
    private FloatingActionButton btnAddSkill;
    private MaterialButton btnSaveProfile, btnCancel;

    // ===== DATA =====
    private final List<String> userSkills = new ArrayList<>();

    // ===== MVVM =====
    private EditProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        bindViews();
        setupViewModel();
        setupListeners();
    }

    // ===============================
    // VIEW BINDING
    // ===============================
    private void bindViews() {

        // Inputs
        etName = findViewById(R.id.et_name);
        etPosition = findViewById(R.id.et_position);
        etLocation = findViewById(R.id.et_location);
        etAbout = findViewById(R.id.et_about);
        etSkill = findViewById(R.id.et_skill);

        // Skills
        chipGroupSkills = findViewById(R.id.chip_group_skills);
        btnAddSkill = findViewById(R.id.btn_add_skill);

        // Actions
        btnSaveProfile = findViewById(R.id.btn_save_profile);
        btnCancel = findViewById(R.id.btn_cancel);

        // Header buttons
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_change_photo)
                .setOnClickListener(v ->
                        Toast.makeText(this, "Change photo coming soon", Toast.LENGTH_SHORT).show()
                );
    }

    // ===============================
    // VIEWMODEL + LOAD DATA
    // ===============================
    private void setupViewModel() {

        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
        ).get(EditProfileViewModel.class);

        viewModel.loadProfile().observe(this, user -> {
            if (user == null) {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                return;
            }

            etName.setText(user.fullName);
            etPosition.setText(user.role);
            etLocation.setText(user.location != null ? user.location : "");
            etAbout.setText(user.bio != null ? user.bio : "");

            userSkills.clear();
            chipGroupSkills.removeAllViews();

            if (user.skills != null) {
                for (String skill : user.skills) {
                    addSkillChip(skill);
                }
            }
        });
    }

    // ===============================
    // LISTENERS
    // ===============================
    private void setupListeners() {

        btnAddSkill.setOnClickListener(v -> {
            String skill = etSkill.getText().toString().trim();
            if (skill.isEmpty()) {
                etSkill.setError("Enter a skill");
                return;
            }
            if (userSkills.contains(skill)) {
                Toast.makeText(this, "Skill already added", Toast.LENGTH_SHORT).show();
                return;
            }
            addSkillChip(skill);
            etSkill.setText("");
        });

        btnSaveProfile.setOnClickListener(v -> saveProfile());

        btnCancel.setOnClickListener(v -> finish());
    }

    // ===============================
    // SKILLS UI
    // ===============================
    private void addSkillChip(String skill) {

        userSkills.add(skill);

        Chip chip = new Chip(this);
        chip.setText(skill);
        chip.setCloseIconVisible(true);

        chip.setOnCloseIconClickListener(v -> {
            chipGroupSkills.removeView(chip);
            userSkills.remove(skill);
        });

        chipGroupSkills.addView(chip);
    }

    // ===============================
    // SAVE PROFILE (API)
    // ===============================
    private void saveProfile() {

        String name = etName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String bio = etAbout.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Name is required");
            return;
        }

        viewModel.updateProfile(name, location, bio)
                .observe(this, success -> {
                    if (success) {
                        viewModel.updateSkills(userSkills);
                        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
