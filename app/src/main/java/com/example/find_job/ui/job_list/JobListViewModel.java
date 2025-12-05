package com.example.find_job.ui.job_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.List;

public class JobListViewModel extends ViewModel {

    private JobRepository repository;
    private LiveData<List<Job>> jobList;

    public JobListViewModel() {
        repository = new JobRepository();
        jobList = repository.fetchJobs();
    }

    public LiveData<List<Job>> getJobs() {
        return jobList;
    }
}
