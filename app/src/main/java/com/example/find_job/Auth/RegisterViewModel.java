package com.example.find_job.Auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;
import com.example.find_job.data.repository.AuthRepository;

public class RegisterViewModel extends ViewModel {

    private final AuthRepository repository = new AuthRepository();

    public LiveData<RegisterResponse> register(String name, String email, String password) {
        return repository.registerUser(new RegisterRequest(name, email, password));
    }
}
