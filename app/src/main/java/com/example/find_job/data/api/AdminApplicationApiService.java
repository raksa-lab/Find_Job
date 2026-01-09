package com.example.find_job.data.api;

import com.example.find_job.data.models.AdminApplicationDetailResponse;
import com.example.find_job.data.models.AdminApplicationsResponse;
import com.example.find_job.data.models.AdminUpdateStatusRequest;
import com.example.find_job.data.models.BaseResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminApplicationApiService {

    // =====================================================
    // GET ALL APPLICATIONS (NO PAGINATION)
    // =====================================================
    @GET("admin/applications")
    Call<AdminApplicationsResponse> getAdminApplications();

    // =====================================================
    // GET ALL APPLICATIONS (WITH PAGINATION)
    // =====================================================
    @GET("admin/applications")
    Call<AdminApplicationsResponse> getAdminApplications(
            @Query("page") int page,
            @Query("limit") int limit
    );

    // =====================================================
    // GET APPLICATION DETAIL
    // =====================================================
    @GET("admin/applications/{id}")
    Call<AdminApplicationDetailResponse> getApplicationDetail(
            @Path("id") String id
    );

    // =====================================================
    // UPDATE APPLICATION STATUS (INTERNAL NOTE)
    // =====================================================
    @PATCH("admin/applications/{id}/status")
    Call<Void> updateApplicationStatus(
            @Path("id") String applicationId,
            @Body AdminUpdateStatusRequest body
    );

    // =====================================================
    // ADMIN → USER REPLY (additionalInfo)
    // ✅ CORRECT ENDPOINT
    // =====================================================
    @PATCH("admin/applications/{id}/notes")
    Call<BaseResponse> replyToUser(
            @Path("id") String applicationId,
            @Body Map<String, Object> body
    );

    // =====================================================
    // DELETE APPLICATION
    // =====================================================
    @DELETE("admin/applications/{id}")
    Call<Void> deleteApplication(
            @Path("id") String applicationId
    );
}
