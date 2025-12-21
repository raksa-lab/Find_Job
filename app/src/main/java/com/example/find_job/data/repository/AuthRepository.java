package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.RegisterRequest;
import com.example.find_job.data.models.RegisterResponse;
import com.example.find_job.data.service.RetrofitClient;
import com.example.find_job.data.service.serviceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private final serviceAPI api;

    // ✅ CONTEXT REQUIRED
    public AuthRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(serviceAPI.class);
    }

    public MutableLiveData<RegisterResponse> registerUser(RegisterRequest request) {

        MutableLiveData<RegisterResponse> result = new MutableLiveData<>();

        api.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(
                    Call<RegisterResponse> call,
                    Response<RegisterResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                result.setValue(null);
            }
        });

        return result;
    }
}
