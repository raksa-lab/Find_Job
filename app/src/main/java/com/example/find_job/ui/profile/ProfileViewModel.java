package com.example.find_job.ui.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.AdminUserStats;
import com.example.find_job.data.models.UserProfileUI;
import com.example.find_job.data.repository.AdminUserRepository;
import com.example.find_job.data.repository.JobRepository;
import com.example.find_job.data.repository.UserRepository;

public class ProfileViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final AdminUserRepository adminUserRepository; // ✅ FIX

    private final MutableLiveData<UserProfileUI> profileLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        jobRepository = new JobRepository(application);
        adminUserRepository = new AdminUserRepository(application); // ✅ FIX
        loadProfile();
    }

    // ===============================
    // ADMIN STATS
    // ===============================

    public LiveData<Integer> getAdminTotalJobs() {
        return jobRepository.fetchTotalJobs();
    }

    public LiveData<AdminUserStats> getAdminUserStats() {
        // ✅ CORRECT ADMIN ENDPOINT
        return adminUserRepository.fetchUserStatsOverview();
    }

    // ===============================
    // PROFILE
    // ===============================

    public LiveData<UserProfileUI> getProfile() {
        return profileLiveData;
    }

    public void loadProfile() {
        userRepository.getFullProfile()
                .observeForever(profileLiveData::setValue);
    }
}
