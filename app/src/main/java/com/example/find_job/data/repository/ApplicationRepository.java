package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.models.ApplicationRequest;
import com.example.find_job.data.models.ApplicationResponse;
import com.example.find_job.data.service.RetrofitClient;
import com.example.find_job.data.service.serviceAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationRepository {

    private final serviceAPI api;

    public ApplicationRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(serviceAPI.class);
    }

    // ======================
    // APPLY JOB
    // ======================
    public LiveData<Boolean> applyJob(
            String jobId,
            ApplicationRequest request
    ) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.applyJob(jobId, request)
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

    // ======================
    // CHECK STATUS
    // ======================
    public LiveData<Boolean> hasApplied(String jobId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.checkApplication(jobId)
                .enqueue(new Callback<Map<String, Boolean>>() {

                    @Override
                    public void onResponse(
                            Call<Map<String, Boolean>> call,
                            Response<Map<String, Boolean>> response
                    ) {
                        result.setValue(
                                response.isSuccessful()
                                        && response.body() != null
                                        && Boolean.TRUE.equals(
                                        response.body().get("applied")
                                )
                        );
                    }

                    @Override
                    public void onFailure(
                            Call<Map<String, Boolean>> call,
                            Throwable t
                    ) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // ======================
    // GET MY APPLICATIONS
    // ======================
    public LiveData<List<ApplicationResponse>> getMyApplications() {
        MutableLiveData<List<ApplicationResponse>> data =
                new MutableLiveData<>();

        api.getMyApplications(
                null, 1, 50, "appliedAt", "desc"
        ).enqueue(new Callback<List<ApplicationResponse>>() {

            @Override
            public void onResponse(
                    Call<List<ApplicationResponse>> call,
                    Response<List<ApplicationResponse>> response
            ) {
                data.setValue(
                        response.isSuccessful()
                                ? response.body()
                                : new ArrayList<>()
                );
            }

            @Override
            public void onFailure(
                    Call<List<ApplicationResponse>> call,
                    Throwable t
            ) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }

    // ======================
    // WITHDRAW APPLICATION
    // ======================
    public LiveData<Boolean> withdraw(String applicationId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.withdrawApplication(applicationId)
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
