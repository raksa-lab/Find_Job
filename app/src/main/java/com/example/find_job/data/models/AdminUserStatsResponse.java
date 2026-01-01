package com.example.find_job.data.models;

import com.google.gson.annotations.SerializedName;

public class AdminUserStatsResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("stats")
    public AdminUserStats stats;
}
