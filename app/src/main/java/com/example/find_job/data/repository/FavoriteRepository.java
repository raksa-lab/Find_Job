package com.example.find_job.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.find_job.data.api.FavoriteApiService;
import com.example.find_job.data.models.FavoriteResponse;
import com.example.find_job.data.models.Job;
import com.example.find_job.data.service.RetrofitClient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteRepository {

    private final FavoriteApiService api;

    // =========================
    // LOCAL CACHE (IMPORTANT)
    // =========================
    private final Set<String> favoriteIds = new HashSet<>();

    public FavoriteRepository(Context context) {
        api = RetrofitClient
                .getClient(context)
                .create(FavoriteApiService.class);

        // Preload favorites once
        syncFavorites();
    }

    // =========================
    // SYNC FAVORITES (API → CACHE)
    // =========================
    private void syncFavorites() {
        api.getFavorites().enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(
                    Call<FavoriteResponse> call,
                    Response<FavoriteResponse> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().favorites != null) {

                    favoriteIds.clear();
                    for (Job job : response.body().favorites) {
                        favoriteIds.add(job.id);
                    }
                }
            }

            @Override
            public void onFailure(
                    Call<FavoriteResponse> call,
                    Throwable t
            ) {
                // ignore
            }
        });
    }

    // =========================
    // CHECK IF FAVORITE ✅ (MISSING METHOD)
    // =========================
    public boolean isFavorite(String jobId) {
        return favoriteIds.contains(jobId);
    }

    // =========================
    // GET FAVORITES (SCREEN)
    // =========================
    public LiveData<List<Job>> getFavoriteJobs() {
        MutableLiveData<List<Job>> data = new MutableLiveData<>();

        api.getFavorites().enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(
                    Call<FavoriteResponse> call,
                    Response<FavoriteResponse> response
            ) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().favorites != null) {

                    data.setValue(response.body().favorites);

                    // keep cache in sync
                    favoriteIds.clear();
                    for (Job job : response.body().favorites) {
                        favoriteIds.add(job.id);
                    }
                } else {
                    data.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(
                    Call<FavoriteResponse> call,
                    Throwable t
            ) {
                data.setValue(new ArrayList<>());
            }
        });

        return data;
    }

    // =========================
    // ADD FAVORITE
    // =========================
    public void addFavorite(String jobId) {
        api.addFavorite(jobId).enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(
                    Call<FavoriteResponse> call,
                    Response<FavoriteResponse> response
            ) {
                if (response.isSuccessful()) {
                    favoriteIds.add(jobId);
                }
            }

            @Override
            public void onFailure(
                    Call<FavoriteResponse> call,
                    Throwable t
            ) { }
        });
    }

    // =========================
    // REMOVE FAVORITE
    // =========================
    public void removeFavorite(String jobId) {
        api.removeFavorite(jobId).enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(
                    Call<FavoriteResponse> call,
                    Response<FavoriteResponse> response
            ) {
                if (response.isSuccessful()) {
                    favoriteIds.remove(jobId);
                }
            }

            @Override
            public void onFailure(
                    Call<FavoriteResponse> call,
                    Throwable t
            ) { }
        });
    }
    // =========================
// CHECK IF FAVORITE (LIVE) ✅
// =========================
    public LiveData<Boolean> isFavoriteLive(String jobId) {

        MutableLiveData<Boolean> result = new MutableLiveData<>();

        api.getFavorites().enqueue(new Callback<FavoriteResponse>() {
            @Override
            public void onResponse(
                    Call<FavoriteResponse> call,
                    Response<FavoriteResponse> response
            ) {
                boolean found = false;

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().favorites != null) {

                    favoriteIds.clear();

                    for (Job job : response.body().favorites) {
                        favoriteIds.add(job.id);
                        if (job.id.equals(jobId)) {
                            found = true;
                        }
                    }
                }

                result.setValue(found);
            }

            @Override
            public void onFailure(Call<FavoriteResponse> call, Throwable t) {
                result.setValue(false);
            }
        });

        return result;
    }

}
