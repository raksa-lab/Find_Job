package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.api.AdminApplicationApiService;
import com.example.find_job.data.models.AdminApplication;
import com.example.find_job.data.models.AdminApplicationDetailResponse;
import com.example.find_job.data.models.AdminApplicationsResponse;
import com.example.find_job.data.models.AdminUpdateStatusRequest;
import com.example.find_job.data.models.BaseResponse;
import com.example.find_job.data.service.RetrofitClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // =====================================================
    // GET ALL APPLICATIONS
    // =====================================================
    public LiveData<List<AdminApplication>> getApplications() {

        MutableLiveData<List<AdminApplication>> result = new MutableLiveData<>();

        api.getAdminApplications(1, 50)
                .enqueue(new Callback<AdminApplicationsResponse>() {
                    @Override
                    public void onResponse(
                            Call<AdminApplicationsResponse> call,
                            Response<AdminApplicationsResponse> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {
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

    // =====================================================
    // GET APPLICATION DETAIL
    // =====================================================
    public LiveData<AdminApplication> getApplicationDetail(String id) {

        MutableLiveData<AdminApplication> result = new MutableLiveData<>();

        api.getApplicationDetail(id)
                .enqueue(new Callback<AdminApplicationDetailResponse>() {
                    @Override
                    public void onResponse(
                            Call<AdminApplicationDetailResponse> call,
                            Response<AdminApplicationDetailResponse> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {
                            result.setValue(response.body().application);
                        } else {
                            result.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<AdminApplicationDetailResponse> call,
                            Throwable t
                    ) {
                        result.setValue(null);
                    }
                });

        return result;
    }

    // =====================================================
    // UPDATE APPLICATION STATUS (INTERNAL NOTE ONLY)
    // =====================================================
    public LiveData<Boolean> updateStatus(
            String applicationId,
            String status,
            String internalNote
    ) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        AdminUpdateStatusRequest body =
                new AdminUpdateStatusRequest(
                        status,
                        internalNote,
                        false
                );

        api.updateApplicationStatus(applicationId, body)
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

    // =====================================================
    // ADMIN → USER NOTE (VISIBLE)
    // ✔ CORRECT PAYLOAD
    // ✔ MATCHES BACKEND
    // =====================================================
    public LiveData<Boolean> replyToUser(
            String applicationId,
            String adminReply
    ) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        Map<String, Object> body = new HashMap<>();
        body.put("notes", adminReply);     // REQUIRED
        body.put("isInternal", false);     // user-visible
        body.put("notifyUser", true);      // send notification

        api.addAdminNote(applicationId, body)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(
                            Call<BaseResponse> call,
                            Response<BaseResponse> response
                    ) {
                        result.setValue(
                                response.isSuccessful()
                                        && response.body() != null
                                        && response.body().success
                        );
                    }

                    @Override
                    public void onFailure(
                            Call<BaseResponse> call,
                            Throwable t
                    ) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // =====================================================
    // DELETE APPLICATION
    // =====================================================
    public LiveData<Boolean> deleteApplication(String applicationId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.deleteApplication(applicationId)
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
