package com.example.find_job.ui.admin.application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.AdminApplication;
import com.example.find_job.data.repository.AdminApplicationRepository;

public class AdminApplicationDetailViewModel
        extends AndroidViewModel {

    private final AdminApplicationRepository repository;

    public AdminApplicationDetailViewModel(
            @NonNull Application app
    ) {
        super(app);
        repository = new AdminApplicationRepository(app);
    }

    // LOAD APPLICATION
    public LiveData<AdminApplication> load(String id) {
        return repository.getApplicationDetail(id);
    }

    // ADMIN → USER REPLY
    public LiveData<Boolean> replyToUser(
            String applicationId,
            String adminReply
    ) {
        return repository.replyToUser(applicationId, adminReply);
    }
}
