package com.example.find_job.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.JobRepository;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final JobRepository repository;

    // Original data from API
    private final LiveData<List<Job>> sourceJobs;

    // Filtered data for UI
    private final MediatorLiveData<List<Job>> filteredJobs = new MediatorLiveData<>();

    private final LiveData<Integer> totalJobs;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        repository = new JobRepository(application);

        sourceJobs = repository.fetchJobs();
        totalJobs = repository.fetchTotalJobs();

        // Observe API data and set initial list
        filteredJobs.addSource(sourceJobs, jobs -> {
            filteredJobs.setValue(jobs);
        });
    }

    // =====================
    // PUBLIC GETTERS
    // =====================
    public LiveData<List<Job>> getJobs() {
        return filteredJobs;
    }

    public LiveData<Integer> getTotalJobs() {
        return totalJobs;
    }

    // =====================
    // SEARCH FUNCTION
    // =====================
    public void searchJobs(String keyword) {

        List<Job> original = sourceJobs.getValue();

        if (original == null) return;

        // If empty → restore original list
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredJobs.setValue(original);
            return;
        }

        String lower = keyword.toLowerCase();
        List<Job> result = new ArrayList<>();

        for (Job job : original) {
            if (
                    (job.title != null && job.title.toLowerCase().contains(lower)) ||
                            (job.company != null && job.company.toLowerCase().contains(lower)) ||
                            (job.location != null && job.location.toLowerCase().contains(lower))
            ) {
                result.add(job);
            }
        }

        filteredJobs.setValue(result);
    }
}
