package com.example.find_job.ui.saved;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.Job;
import com.example.find_job.data.repository.FavoriteRepository;

import java.util.List;

public class SavedJobsViewModel extends ViewModel {

    private FavoriteRepository repository;
    private LiveData<List<Job>> savedJobs;

    public void init(Context context) {
        repository = new FavoriteRepository(context);
        savedJobs = repository.getFavoriteJobs();
    }

    public LiveData<List<Job>> getSavedJobs() {
        return savedJobs;
    }

    public void removeSavedJob(String jobId) {
        repository.removeFavorite(jobId);
    }
}
