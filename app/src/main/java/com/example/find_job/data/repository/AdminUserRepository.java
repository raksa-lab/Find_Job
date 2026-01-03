package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.api.AdminUserApiService;
import com.example.find_job.data.models.AdminUser;
import com.example.find_job.data.models.AdminUserStats;
import com.example.find_job.data.models.AdminUserStatsResponse;
import com.example.find_job.data.models.DeleteUserRequest;
import com.example.find_job.data.models.UserListResponse;
import com.example.find_job.data.service.RetrofitClient;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserRepository {

    private final AdminUserApiService api;

    public AdminUserRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(AdminUserApiService.class);
    }

    // ===============================
    // FETCH USERS
    // ===============================
    public LiveData<List<AdminUser>> fetchUsers() {

        MutableLiveData<List<AdminUser>> live = new MutableLiveData<>();

        api.getAllUsers(null, null, null, null, 1, 50)
                .enqueue(new Callback<UserListResponse>() {
                    @Override
                    public void onResponse(
                            Call<UserListResponse> call,
                            Response<UserListResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            live.setValue(response.body().users);
                        } else {
                            live.setValue(Collections.emptyList());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserListResponse> call, Throwable t) {
                        live.setValue(Collections.emptyList());
                    }
                });

        return live;
    }

    // ===============================
    // DISABLE USER (soft delete)
    // ===============================
    public LiveData<Boolean> disableUser(String userId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        DeleteUserRequest body =
                new DeleteUserRequest("Admin disabled user", true);

        api.updateDeleteState(userId, body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        result.setValue(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // ===============================
    // ENABLE USER (restore)
    // ===============================
    public LiveData<Boolean> enableUser(String userId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        DeleteUserRequest body =
                new DeleteUserRequest("Admin restored user", false);

        api.updateDeleteState(userId, body)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        result.setValue(response.isSuccessful());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        result.setValue(false);
                    }
                });

        return result;
    }

    // ===============================
    // USER STATS
    // ===============================
    public LiveData<AdminUserStats> fetchUserStatsOverview() {

        MutableLiveData<AdminUserStats> live = new MutableLiveData<>();

        api.getUserStatsOverview(null)
                .enqueue(new Callback<AdminUserStatsResponse>() {
                    @Override
                    public void onResponse(
                            Call<AdminUserStatsResponse> call,
                            Response<AdminUserStatsResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            live.setValue(response.body().stats);
                        } else {
                            live.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<AdminUserStatsResponse> call, Throwable t) {
                        live.setValue(null);
                    }
                });

        return live;
    }
}
