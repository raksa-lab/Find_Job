package com.example.find_job.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private final JobRepository repository;
    private final LiveData<List<Job>> jobs;

    public HomeViewModel() {
        repository = new JobRepository();
        jobs = repository.fetchJobs(); // CALL API HERE
    }

    public LiveData<List<Job>> getJobs() {
        return jobs;
    }
}
