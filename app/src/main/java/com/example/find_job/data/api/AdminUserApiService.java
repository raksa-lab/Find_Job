package com.example.find_job.data.api;

import com.example.find_job.data.models.AdminUserStatsResponse;
import com.example.find_job.data.models.DeleteUserRequest;
import com.example.find_job.data.models.UserListResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminUserApiService {

    @GET("admin/users")
    Call<UserListResponse> getAllUsers(
            @Query("search") String search,
            @Query("role") String role,
            @Query("verified") Boolean verified,
            @Query("banned") Boolean banned,
            @Query("page") int page,
            @Query("limit") int limit
    );
    @HTTP(method = "DELETE", path = "admin/users/{id}", hasBody = true)
    Call<Void> deleteUser(
            @Path("id") String userId,
            @Body DeleteUserRequest body
    );
    @GET("admin/users/stats/overview")
    Call<AdminUserStatsResponse> getUserStatsOverview(
            @Query("timeRange") String timeRange
    );


}
