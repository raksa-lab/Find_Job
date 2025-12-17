package com.example.find_job.ui.job_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.List;

public class JobListViewModel extends ViewModel {

    private final JobRepository repository = new JobRepository();
    private final MutableLiveData<List<Job>> jobList = new MutableLiveData<>();

    public JobListViewModel() {
        refreshJobs(); // initial load
    }

    public LiveData<List<Job>> getJobs() {
        return jobList;
    }

    // 🔥 FORCE API REFRESH
    public void refreshJobs() {
        repository.fetchJobs().observeForever(jobList::setValue);
    }
}
