package com.example.find_job.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final JobRepository repository;
    private final LiveData<List<Job>> jobs;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        // ✅ PASS CONTEXT
        repository = new JobRepository(application);
        jobs = repository.fetchJobs();
    }

    public LiveData<List<Job>> getJobs() {
        return jobs;
    }
}
