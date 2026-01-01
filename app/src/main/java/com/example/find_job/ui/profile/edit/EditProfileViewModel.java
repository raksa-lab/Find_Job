package com.example.find_job.ui.profile.edit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.UserProfile;
import com.example.find_job.data.repository.UserRepository;

import java.util.List;

public class EditProfileViewModel extends AndroidViewModel {

    private final UserRepository repository;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    // ===============================
    // LOAD PROFILE
    // ===============================
    public LiveData<UserProfile> loadProfile() {
        return repository.getMyProfile();
    }

    // ===============================
    // UPDATE BASIC PROFILE
    // ===============================
    public LiveData<Boolean> updateProfile(
            String name,
            String location,
            String bio
    ) {
        return repository.updateProfile(name, location, bio);
    }

    // ===============================
    // UPDATE SKILLS
    // ===============================
    public void updateSkills(List<String> skills) {
        repository.updateSkills(skills);
    }


}
