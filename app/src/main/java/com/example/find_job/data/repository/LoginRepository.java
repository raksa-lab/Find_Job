package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.LoginRequest;
import com.example.find_job.data.models.LoginResponse;
import com.example.find_job.data.service.RetrofitClient;
import com.example.find_job.data.service.serviceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private final serviceAPI api;

    public LoginRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(serviceAPI.class);
    }

    public MutableLiveData<LoginResponse> login(String email, String password) {

        MutableLiveData<LoginResponse> data = new MutableLiveData<>();

        LoginRequest request = new LoginRequest(email, password);

        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(
                    Call<LoginResponse> call,
                    Response<LoginResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
