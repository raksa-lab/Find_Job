package com.example.find_job.ui.application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.data.repository.ApplicationRepository;

import java.util.List;

public class AppliedJobsViewModel extends AndroidViewModel {

    private final ApplicationRepository repository;

    public AppliedJobsViewModel(@NonNull Application application) {
        super(application);
        repository = new ApplicationRepository(application);
    }

    public LiveData<List<AppliedJob>> getAppliedJobs() {
        return repository.getMyApplications();
    }

    public LiveData<Boolean> delete(String applicationId) {
        return repository.deleteApplication(applicationId);
    }

}
