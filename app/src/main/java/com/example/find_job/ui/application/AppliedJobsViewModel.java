package com.example.find_job.ui.application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.data.models.ApplicationNotesResponse;
import com.example.find_job.data.repository.ApplicationRepository;

import java.util.List;

public class AppliedJobsViewModel extends AndroidViewModel {

    private final ApplicationRepository repository;

    private final MutableLiveData<Boolean> reloadTrigger =
            new MutableLiveData<>();

    public AppliedJobsViewModel(@NonNull Application application) {
        super(application);
        repository = new ApplicationRepository(application);
        reloadTrigger.setValue(true);
    }

    // ✅ MUST return AppliedJob
    public LiveData<List<AppliedJob>> getAppliedJobs() {
        return Transformations.switchMap(
                reloadTrigger,
                trigger -> repository.getMyApplications()
        );
    }

    public void reload() {
        reloadTrigger.setValue(true);
    }

    public LiveData<Boolean> delete(String applicationId) {
        return repository.deleteApplication(applicationId);
    }

    public LiveData<ApplicationNotesResponse> getApplicationNotes(
            String applicationId
    ) {
        return repository.getApplicationNotes(applicationId);
    }

    public LiveData<Boolean> updateUserNote(
            String applicationId,
            String note
    ) {
        return repository.updateUserNote(applicationId, note);
    }
}
