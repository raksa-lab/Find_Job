package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.api.AdminApplicationApiService;
import com.example.find_job.data.models.AdminApplication;
import com.example.find_job.data.models.AdminApplicationsResponse;
import com.example.find_job.data.models.AdminUpdateStatusRequest;
import com.example.find_job.data.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminApplicationRepository {

    private final AdminApplicationApiService api;

    public AdminApplicationRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(AdminApplicationApiService.class);
    }

    // ===============================
    // LOAD ADMIN APPLICATIONS
    // ===============================
    public LiveData<List<AdminApplication>> getApplications() {

        MutableLiveData<List<AdminApplication>> result = new MutableLiveData<>();

        api.getAdminApplications(1, 50)
                .enqueue(new Callback<AdminApplicationsResponse>() {
                    @Override
                    public void onResponse(
                            Call<AdminApplicationsResponse> call,
                            Response<AdminApplicationsResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(response.body().applications);
                        } else {
                            result.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<AdminApplicationsResponse> call,
                            Throwable t
                    ) {
                        result.setValue(null);
                    }
                });

        return result;
    }

    public LiveData<Boolean> deleteApplication(String id) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.deleteApplication(id)
                .enqueue(new retrofit2.Callback<Void>() {

                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response
                    ) {
                        result.setValue(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t
                    ) {
                        result.setValue(false);
                    }
                });

        return result;
    }


    // ===============================
    // UPDATE STATUS
    // ===============================
    public LiveData<Boolean> updateStatus(String id, String status) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        AdminUpdateStatusRequest body =
                new AdminUpdateStatusRequest(status);

        api.updateApplicationStatus(id, body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response
                    ) {
                        result.setValue(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t
                    ) {
                        result.setValue(false);
                    }
                });

        return result;
    }
}
