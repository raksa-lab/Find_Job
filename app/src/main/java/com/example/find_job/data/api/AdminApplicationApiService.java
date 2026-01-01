package com.example.find_job.data.api;

import com.example.find_job.data.models.AdminApplicationsResponse;
import com.example.find_job.data.models.AdminUpdateStatusRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminApplicationApiService {

    @GET("admin/applications")
    Call<AdminApplicationsResponse> getAdminApplications();

    // ✅ GET ALL APPLICATIONS
    @GET("admin/applications")
    Call<AdminApplicationsResponse> getAdminApplications(
            @Query("page") int page,
            @Query("limit") int limit
    );

    // ✅ UPDATE STATUS (CORRECT ENDPOINT)
    @PATCH("admin/applications/{id}/status")
    Call<Void> updateApplicationStatus(
            @Path("id") String applicationId,
            @Body AdminUpdateStatusRequest body
    );
    // ✅ DELETE APPLICATION
    @DELETE("admin/applications/{id}")
    Call<Void> deleteApplication(
            @Path("id") String applicationId
    );



}
