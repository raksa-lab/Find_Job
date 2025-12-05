package com.example.find_job.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;
import com.example.find_job.data.service.RetrofitClient;
import com.example.find_job.data.service.serviceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final serviceAPI api = RetrofitClient.getClient().create(serviceAPI.class);

    public MutableLiveData<RegisterResponse> registerUser(RegisterRequest request) {

        MutableLiveData<RegisterResponse> data = new MutableLiveData<>();

        api.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.setValue(response.body());  // SUCCESS
                } else {
                    RegisterResponse error = new RegisterResponse();
                    error.status = response.code();
                    error.error = "Register failed";
                    data.setValue(error); // RETURN ERROR
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                RegisterResponse error = new RegisterResponse();
                error.error = t.getMessage();
                data.setValue(error); // NETWORK ERROR
            }
        });

        return data;
    }
}

