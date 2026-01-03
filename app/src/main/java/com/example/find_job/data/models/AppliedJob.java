package com.example.find_job.data.models;

public class AppliedJob {

    public String id;
    public String status;
    public String stage;

    // Job info
    public String jobId;
    public String jobTitle;
    public String jobCompany;

    // User info
    public String userId;
    public String userName;
    public String userEmail;
    public String userPhone;

    // Application content
    public String coverLetter;   // what user wrote
    public String notes;         // admin note ✅ ADD THIS

    // Resume
    public String resumeUrl;

    // Nested job object
    public Job job;
}
