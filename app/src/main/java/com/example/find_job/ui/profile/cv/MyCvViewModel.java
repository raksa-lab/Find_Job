package com.example.find_job.ui.profile.cv;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.repository.UserRepository;

import java.io.File;

public class MyCvViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public MyCvViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public LiveData<Boolean> hasResume() {
        return repository.hasResume();
    }

    public LiveData<Boolean> uploadResume(File file, String mimeType) {
        return repository.uploadResume(file, mimeType);
    }


    public LiveData<Boolean> deleteResume() {
        return repository.deleteResume();
    }
}
