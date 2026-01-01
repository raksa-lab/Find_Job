package com.example.find_job.ui.application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.repository.ApplicationRepository;
import com.example.find_job.data.repository.UserRepository;

public class ApplyJobViewModel extends AndroidViewModel {

    private final ApplicationRepository applicationRepo;
    private final UserRepository userRepo;

    public ApplyJobViewModel(@NonNull Application app) {
        super(app);
        applicationRepo = new ApplicationRepository(app);
        userRepo = new UserRepository(app);
    }

    // ===============================
    // CHECK CV EXISTS
    // ===============================
    public LiveData<Boolean> hasResume() {
        return userRepo.hasResume();
    }

    // ===============================
    // CHECK ALREADY APPLIED
    // ===============================
    public LiveData<Boolean> hasApplied(String jobId) {
        return applicationRepo.hasApplied(jobId);
    }

    // ===============================
    // APPLY JOB  ✅ FIXED
    // ===============================
    public LiveData<Boolean> apply(String jobId, String coverLetter) {
        return applicationRepo.apply(jobId, coverLetter);
    }
}
