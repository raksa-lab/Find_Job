package com.example.find_job.data.models;

public class Application {

    public String id;

    public String jobId;
    public String jobTitle;
    public String jobCompany;

    public String status;
    public String stage;
    public boolean viewedByAdmin;

    public Job job; // ✅ USE EXISTING Job MODEL

    public AppliedAt appliedAt;
    public AppliedAt lastUpdated;

    public static class AppliedAt {
        public long _seconds;
        public long _nanoseconds;
    }
}
