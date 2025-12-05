package com.example.find_job.Auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.find_job.data.models.LoginResponse;
import com.example.find_job.data.repository.LoginRepository;

public class LoginViewModel extends ViewModel {

    private LoginRepository repository;

    public LoginViewModel() {
        repository = new LoginRepository();
    }

    public LiveData<LoginResponse> loginUser(String email, String password) {
        return repository.login(email, password);
    }
}
