package com.example.find_job.Auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.find_job.data.models.LoginResponse;
import com.example.find_job.data.repository.LoginRepository;
import com.example.find_job.utils.SessionManager;

public class LoginViewModel extends AndroidViewModel {

    private final LoginRepository repository;
    private final SessionManager sessionManager;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(application);

        sessionManager = new SessionManager(application);
    }

    public LiveData<LoginResponse> loginUser(String email, String password) {

        LiveData<LoginResponse> response = repository.login(email, password);

        response.observeForever(loginResponse -> {
            if (loginResponse != null && loginResponse.success) {

                // ✅ SAVE SESSION CORRECTLY
                sessionManager.saveSession(
                        loginResponse.idToken,              // token
                        loginResponse.uid.hashCode(),       // local int ID
                        loginResponse.user.role             // admin / seeker
                );
            }
        });

        return response;
    }
}
