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

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.R;
import com.example.find_job.ui.jobs.AddJobActivity;
import com.example.find_job.ui.saved.SavedJobsActivity;
import com.example.find_job.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private ActivityResultLauncher<Intent> addJobLauncher;

    private ImageView ivProfile;
    private TextView tvName, tvPosition, tvLocation, tvAbout;
    private TextView tvApplications, tvSaved, tvViews;

    private MaterialButton btnEditProfile, btnLogout;

    private View cardApplications, cardSaved, cardCV;
    private LinearLayout menuAccount, menuNotifications, menuPrivacy, menuHelp;

    // ===============================
    // 🔐 ROUTE PROTECTION
    // ===============================
    @Override
    public void onStart() {
        super.onStart();

        SessionManager sessionManager = new SessionManager(requireContext());

        // Not logged in → redirect
        if (!sessionManager.isLoggedIn()) {
            Intent i = new Intent(requireContext(), LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            requireActivity().finish();
            return;
        }

        // Job seeker should NOT post jobs
        if (sessionManager.isJobSeeker()) {
            cardApplications.setVisibility(View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.profile, container, false);

        bindViews(view);
        setupActivityResult();
        setupClickListeners();
        loadUserData();

        return view;
    }

    private void setupActivityResult() {
        addJobLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK) {
                        showToast("Job list updated");
                    }
                }
        );
    }

    private void bindViews(View view) {

        ivProfile = view.findViewById(R.id.iv_profile_image);
        tvName = view.findViewById(R.id.tv_user_name);
        tvPosition = view.findViewById(R.id.tv_user_position);
        tvLocation = view.findViewById(R.id.tv_user_location);
        tvAbout = view.findViewById(R.id.tv_about);

        tvApplications = view.findViewById(R.id.tv_applications_count);
        tvSaved = view.findViewById(R.id.tv_saved_count);
        tvViews = view.findViewById(R.id.tv_views_count);

        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout);

        cardApplications = view.findViewById(R.id.card_applications);
        cardSaved = view.findViewById(R.id.card_saved);
        cardCV = view.findViewById(R.id.card_CV);

        menuAccount = view.findViewById(R.id.menu_account);
        menuNotifications = view.findViewById(R.id.menu_notifications);
        menuPrivacy = view.findViewById(R.id.menu_privacy);
        menuHelp = view.findViewById(R.id.menu_help);
    }

    private void setupClickListeners() {

        // ADMIN ONLY
        cardApplications.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddJobActivity.class);
            addJobLauncher.launch(intent);
        });

        // JOB SEEKER
        cardSaved.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), SavedJobsActivity.class))
        );

        cardCV.setOnClickListener(v -> showToast("CV"));

        btnEditProfile.setOnClickListener(v ->
                showToast("Edit profile")
        );

        btnLogout.setOnClickListener(v -> logoutUser());

        menuAccount.setOnClickListener(v -> showToast("Account settings"));
        menuNotifications.setOnClickListener(v -> showToast("Notifications"));
        menuPrivacy.setOnClickListener(v -> showToast("Privacy & security"));
        menuHelp.setOnClickListener(v -> showToast("Help & support"));
    }

    // ===============================
    // LOGOUT (CLEAN)
    // ===============================
    private void logoutUser() {
        SessionManager sessionManager = new SessionManager(requireContext());
        sessionManager.logout();

        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    // ===============================
    // MOCK DATA
    // ===============================
    private void loadUserData() {
        tvName.setText("John Doe");
        tvPosition.setText("Senior Product Designer");
        tvLocation.setText("📍 San Francisco, CA");

        tvApplications.setText("24");
        tvSaved.setText("18");
        tvViews.setText("142");

        tvAbout.setText(
                "Passionate product designer with 5+ years of experience creating user-centered digital experiences."
        );
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
