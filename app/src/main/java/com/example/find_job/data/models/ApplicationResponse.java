package com.example.find_job.data.models;

import com.google.gson.annotations.SerializedName;

public class ApplicationResponse {

    @SerializedName("success")
    public boolean success;

    @SerializedName("hasApplied")
    public boolean hasApplied;

    @SerializedName("canApply")
    public boolean canApply;
}
