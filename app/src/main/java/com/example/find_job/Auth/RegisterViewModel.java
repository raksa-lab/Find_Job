package com.example.find_job.Auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;
import com.example.find_job.data.repository.AuthRepository;

public class RegisterViewModel extends AndroidViewModel {

    private final AuthRepository repository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        // ✅ PASS CONTEXT
        repository = new AuthRepository(application);
    }

    public LiveData<RegisterResponse> register(
            String name,
            String email,
            String password
    ) {
        return repository.registerUser(
                new RegisterRequest(name, email, password)
        );
    }
}
