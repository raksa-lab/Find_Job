package com.example.find_job.ui.job_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.List;

public class JobListViewModel extends AndroidViewModel {

    private final JobRepository repository;
    private final MutableLiveData<List<Job>> jobList = new MutableLiveData<>();

    public JobListViewModel(@NonNull Application application) {
        super(application);

        // ✅ PASS CONTEXT
        repository = new JobRepository(application);
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
