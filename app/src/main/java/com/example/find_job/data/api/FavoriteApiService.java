package com.example.find_job.data.api;

import com.example.find_job.data.models.FavoriteResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteApiService {

    // ✅ GET FAVORITES
    @GET("favourites")
    Call<FavoriteResponse> getFavorites();

    // ✅ ADD FAVORITE
    @POST("favourites/{jobId}")
    Call<FavoriteResponse> addFavorite(
            @Path("jobId") String jobId
    );

    // ✅ REMOVE FAVORITE
    @DELETE("favourites/{jobId}")
    Call<FavoriteResponse> removeFavorite(
            @Path("jobId") String jobId
    );
}
