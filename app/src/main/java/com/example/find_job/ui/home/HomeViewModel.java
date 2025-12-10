package com.example.find_job.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private JobRepository repository = new JobRepository();
    private MutableLiveData<List<Job>> jobs;

    public LiveData<List<Job>> getJobs() {
        if (jobs == null) jobs = repository.fetchJobs();
        return jobs;
    }
}

