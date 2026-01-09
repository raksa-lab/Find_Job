package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.api.ApplicationApiService;
import com.example.find_job.data.models.AppliedJob;
import com.example.find_job.data.models.AppliedJobsResponse;
import com.example.find_job.data.models.ApplyRequest;
import com.example.find_job.data.models.ApplicationNotesResponse;
import com.example.find_job.data.models.ApplicationResponse;
import com.example.find_job.data.models.BaseResponse;
import com.example.find_job.data.service.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationRepository {

    private final ApplicationApiService api;

    public ApplicationRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(ApplicationApiService.class);
    }

    // =====================================================
    // APPLY JOB (WITH OPTIONAL NOTE)
    // =====================================================
    public LiveData<Boolean> apply(
            String jobId,
            String coverLetter,
            String userNote
    ) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.applyJob(jobId, new ApplyRequest(coverLetter, userNote))
                .enqueue(new Callback<ApplicationResponse>() {
                    @Override
                    public void onResponse(
                            Call<ApplicationResponse> call,
                            Response<ApplicationResponse> response
                    ) {
                        result.setValue(
                                response.isSuccessful()
                                        && response.body() != null
                                        && response.body().success
                        );
                    }

                    @Override
                    public void onFailure(Call<ApplicationResponse> call, Throwable t) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // =====================================================
    // CHECK IF USER ALREADY APPLIED
    // =====================================================
    public LiveData<Boolean> hasApplied(String jobId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.checkStatus(jobId)
                .enqueue(new Callback<ApplicationResponse>() {
                    @Override
                    public void onResponse(
                            Call<ApplicationResponse> call,
                            Response<ApplicationResponse> response
                    ) {
                        result.setValue(
                                response.isSuccessful()
                                        && response.body() != null
                                        && response.body().hasApplied
                        );
                    }

                    @Override
                    public void onFailure(Call<ApplicationResponse> call, Throwable t) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // =====================================================
    // WITHDRAW APPLICATION
    // =====================================================
    public LiveData<Boolean> deleteApplication(String applicationId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.withdraw(applicationId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response
                    ) {
                        result.setValue(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // =====================================================
    // GET MY APPLICATIONS
    // =====================================================
    public LiveData<List<AppliedJob>> getMyApplications() {

        MutableLiveData<List<AppliedJob>> result = new MutableLiveData<>();

        api.getMyApplications(null, 1, 20)
                .enqueue(new Callback<AppliedJobsResponse>() {
                    @Override
                    public void onResponse(
                            Call<AppliedJobsResponse> call,
                            Response<AppliedJobsResponse> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {

                            result.setValue(response.body().applications);
                        } else {
                            result.setValue(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<AppliedJobsResponse> call,
                            Throwable t
                    ) {
                        result.setValue(new ArrayList<>());
                    }
                });

        return result;
    }

    // =====================================================
    // GET APPLICATION NOTES (USER + ADMIN NOTES)
    // =====================================================
    public LiveData<ApplicationNotesResponse> getApplicationNotes(
            String applicationId
    ) {

        MutableLiveData<ApplicationNotesResponse> result =
                new MutableLiveData<>();

        api.getNotes(applicationId)
                .enqueue(new Callback<ApplicationNotesResponse>() {
                    @Override
                    public void onResponse(
                            Call<ApplicationNotesResponse> call,
                            Response<ApplicationNotesResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            result.setValue(response.body());
                        } else {
                            result.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ApplicationNotesResponse> call,
                            Throwable t
                    ) {
                        result.setValue(null);
                    }
                });

        return result;
    }

    // =====================================================
    // UPDATE USER NOTE
    // =====================================================
    public LiveData<Boolean> updateUserNote(
            String applicationId,
            String note
    ) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        Map<String, Object> body = new HashMap<>();
        Map<String, String> notes = new HashMap<>();
        notes.put("userNotes", note);
        body.put("notes", notes);

        api.updateUserNotes(applicationId, body)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(
                            Call<BaseResponse> call,
                            Response<BaseResponse> response
                    ) {
                        result.setValue(response.isSuccessful());
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

}
