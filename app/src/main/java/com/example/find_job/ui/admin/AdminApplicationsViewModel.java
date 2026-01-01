package com.example.find_job.ui.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.AdminApplication;
import com.example.find_job.data.repository.AdminApplicationRepository;

import java.util.List;

public class AdminApplicationsViewModel extends AndroidViewModel {

    private final AdminApplicationRepository repository;
    public LiveData<Boolean> deleteApplication(String id) {
        return repository.deleteApplication(id);
    }

    public AdminApplicationsViewModel(@NonNull Application application) {
        super(application);
        repository = new AdminApplicationRepository(application);
    }

    public LiveData<List<AdminApplication>> getApplications() {
        return repository.getApplications();
    }

    public LiveData<Boolean> updateStatus(String id, String status) {
        return repository.updateStatus(id, status); // ✅ NOW MATCHES
    }
}
