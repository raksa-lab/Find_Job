package com.example.find_job.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.MainActivity;
import com.example.find_job.R;
import com.example.find_job.data.models.UserProfileUI;
import com.example.find_job.ui.admin.users.AdminUserListActivity;
import com.example.find_job.ui.jobs.AddJobActivity;
import com.example.find_job.ui.profile.cv.MyCvActivity;
import com.example.find_job.ui.profile.edit.EditProfileActivity;
import com.example.find_job.ui.saved.SavedJobsActivity;
import com.example.find_job.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class ProfileFragment extends Fragment {

    // ===============================
    // PROFILE
    // ===============================
    private ImageView ivProfile;
    private TextView tvName, tvPosition, tvLocation, tvAbout;
    private ChipGroup chipGroupSkills;

    private MaterialButton btnEditProfile, btnLogout;

    // ===============================
    // QUICK ACTIONS
    // ===============================
    private View cardApplications;
    private View cardSaved;
    private View cardCV;

    // ===============================
    // ADMIN OVERVIEW
    // ===============================
    private View adminOverviewTitle, adminCards;
    private View cardTotalJobs, cardTotalUsers;
    private TextView tvTotalJobs, tvTotalUsers;

    private ProfileViewModel viewModel;
    private ActivityResultLauncher<Intent> editProfileLauncher;

    // ===============================
    // LIFECYCLE
    // ===============================
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.profile, container, false);

        bindViews(view);
        setupViewModel();
        setupLauncher();
        setupClickListeners();

        return view;
    }

    // ===============================
    // VIEWMODEL
    // ===============================
    private void setupViewModel() {
        viewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(ProfileViewModel.class);

        viewModel.getProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile == null) {
                showToast("Failed to load profile");
                return;
            }
            bindProfile(profile);
        });
    }

    private void setupLauncher() {
        editProfileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        viewModel.loadProfile();
                    }
                }
        );
    }

    // ===============================
    // BIND PROFILE
    // ===============================
    private void bindProfile(UserProfileUI profile) {

        tvName.setText(profile.fullName);
        tvPosition.setText(
                "employee".equalsIgnoreCase(profile.role)
                        ? "Job Seeker"
                        : profile.role
        );

        tvLocation.setText(
                profile.location != null
                        ? "📍 " + profile.location
                        : "📍 Not set"
        );

        tvAbout.setText(
                profile.bio != null && !profile.bio.isEmpty()
                        ? profile.bio
                        : "Tell us about yourself"
        );

        boolean isAdmin = "admin".equalsIgnoreCase(profile.role);

        // QUICK ACTION VISIBILITY
        cardApplications.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        cardSaved.setVisibility(isAdmin ? View.GONE : View.VISIBLE);
        cardCV.setVisibility(isAdmin ? View.GONE : View.VISIBLE);

        // ADMIN OVERVIEW
        adminOverviewTitle.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        adminCards.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        adjustQuickActionsLayout(isAdmin);
        renderSkills(profile.skills);

        if (isAdmin) {
            loadAdminStats();
        }
    }

    // ===============================
    // ADMIN STATS
    // ===============================
    private void loadAdminStats() {

        // ✅ TOTAL JOBS (DISPLAY ONLY)
        viewModel.getAdminTotalJobs().observe(getViewLifecycleOwner(), total -> {
            if (total != null) {
                tvTotalJobs.setText(String.valueOf(total));
            }
        });

        // ✅ TOTAL USERS (ADMIN)
        viewModel.getAdminUserStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                tvTotalUsers.setText(String.valueOf(stats.totalUsers));
            }
        });

    }


    // ===============================
    // SKILLS
    // ===============================
    private void renderSkills(List<String> skills) {
        chipGroupSkills.removeAllViews();

        if (skills == null || skills.isEmpty()) {
            Chip chip = new Chip(requireContext());
            chip.setText("No skills added");
            chip.setEnabled(false);
            chipGroupSkills.addView(chip);
            return;
        }

        for (String skill : skills) {
            Chip chip = new Chip(requireContext());
            chip.setText(skill);
            chip.setClickable(false);
            chipGroupSkills.addView(chip);
        }
    }

    // ===============================
    // BIND VIEWS
    // ===============================
    private void bindViews(View view) {

        ivProfile = view.findViewById(R.id.iv_profile_image);
        tvName = view.findViewById(R.id.tv_user_name);
        tvPosition = view.findViewById(R.id.tv_user_position);
        tvLocation = view.findViewById(R.id.tv_user_location);
        tvAbout = view.findViewById(R.id.tv_about);
        chipGroupSkills = view.findViewById(R.id.chip_group_skills);

        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        cardApplications = view.findViewById(R.id.card_applications);
        cardSaved = view.findViewById(R.id.card_saved);
        cardCV = view.findViewById(R.id.card_CV);

        adminOverviewTitle = view.findViewById(R.id.tv_admin_overview);
        adminCards = view.findViewById(R.id.layout_admin_cards);

        cardTotalJobs = view.findViewById(R.id.card_total_jobs);
        cardTotalUsers = view.findViewById(R.id.card_total_users); // ✅ FIX

        tvTotalJobs = view.findViewById(R.id.tv_total_jobs);
        tvTotalUsers = view.findViewById(R.id.tv_total_users);
    }


    // ===============================
    // CLICK LISTENERS
    // ===============================
    private void setupClickListeners() {

        btnEditProfile.setOnClickListener(v ->
                editProfileLauncher.launch(
                        new Intent(requireContext(), EditProfileActivity.class)
                )
        );

        if (cardSaved != null) {
            cardSaved.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), SavedJobsActivity.class))
            );
        }

        if (cardCV != null) {
            cardCV.setOnClickListener(v ->
                    startActivity(new Intent(requireContext(), MyCvActivity.class))
            );
        }

        if (cardApplications != null) {
            cardApplications.setOnClickListener(v -> {
                if (!new SessionManager(requireContext()).isAdmin()) {
                    showToast("Admin only");
                    return;
                }
                startActivity(new Intent(requireContext(), AddJobActivity.class));
            });
        }

        // ✅ ADMIN ONLY (CRASH FIX)
        if (cardTotalUsers != null) {
            cardTotalUsers.setOnClickListener(v ->
                    startActivity(
                            new Intent(requireContext(),
                                    com.example.find_job.ui.admin.users.AdminUserListActivity.class)
                    )
            );
        }

        btnLogout.setOnClickListener(v -> logoutUser());
    }



    // ===============================
    // LAYOUT UTILS
    // ===============================
    private void adjustQuickActionsLayout(boolean isAdmin) {
        if (isAdmin) {
            cardApplications.setLayoutParams(
                    new LinearLayout.LayoutParams(dpToPx(120), dpToPx(100))
            );
        } else {
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(0, dpToPx(100), 1);
            cardSaved.setLayoutParams(params);
            cardCV.setLayoutParams(params);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    // ===============================
    // LOGOUT
    // ===============================
    private void logoutUser() {
        new SessionManager(requireContext()).logout();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
