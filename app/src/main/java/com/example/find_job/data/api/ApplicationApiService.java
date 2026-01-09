package com.example.find_job.data.api;

import com.example.find_job.data.models.ApplicationNotesResponse;
import com.example.find_job.data.models.AppliedJobsResponse;
import com.example.find_job.data.models.ApplyRequest;
import com.example.find_job.data.models.ApplicationResponse;
import com.example.find_job.data.models.BaseResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApplicationApiService {

    // =====================================================
    // APPLY JOB WITH COVER LETTER + USER NOTE
    // =====================================================
    @POST("apply/{jobId}/apply")
    Call<ApplicationResponse> applyJob(
            @Path("jobId") String jobId,
            @Body ApplyRequest body
    );

    // =====================================================
    // GET USER APPLICATIONS
    // =====================================================
    @GET("apply")
    Call<AppliedJobsResponse> getMyApplications(
            @Query("status") String status,
            @Query("page") int page,
            @Query("limit") int limit
    );

    // =====================================================
    // CHECK IF USER ALREADY APPLIED
    // =====================================================
    @GET("apply/check/{jobId}")
    Call<ApplicationResponse> checkStatus(
            @Path("jobId") String jobId
    );

    // =====================================================
    // WITHDRAW APPLICATION
    // =====================================================
    @DELETE("apply/{applicationId}")
    Call<Void> withdraw(
            @Path("applicationId") String applicationId
    );

    // =====================================================
    // GET USER + ADMIN NOTES
    // =====================================================
    @GET("apply/{appId}/notes")
    Call<ApplicationNotesResponse> getNotes(
            @Path("appId") String appId
    );

    // =====================================================
    // UPDATE USER NOTES  ✅ FIXED
    // =====================================================
    @POST("apply/{appId}/notes")
    Call<BaseResponse> updateUserNotes(
            @Path("appId") String appId,
            @Body Map<String, Object> body   // ✅ CORRECT
    );

    // =========================
    // GET NOTES (ADMIN)
    // =========================
    @GET("admin/applications/{appId}/notes")
    Call<ApplicationNotesResponse> getAdminNotes(
            @Path("appId") String appId
    );

    // =========================
    // ADD ADMIN NOTE
    // =========================
    @POST("admin/applications/{appId}/notes")
    Call<BaseResponse> addAdminNote(
            @Path("appId") String appId,
            @Body Map<String, String> body
    );
}
