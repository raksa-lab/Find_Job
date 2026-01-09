package com.example.find_job.data.models;

import java.util.List;

public class JobRequest {

    // BASIC
    public String title;
    public String description;
    public String company;
    public String location;

    // JOB TYPE
    public String employmentType;     // internship, full-time
    public String experienceLevel;    // entry, mid, senior
    public String category;

    // WORK MODE
    public boolean remote;

    // COMPENSATION
    public int salary;
    public String companyLogo;

    // APPLY
    public String applicationLink;

    // LISTS
    public List<String> requirements;
    public List<String> skills;
    public List<String> benefits;
    public List<String> tags;

    // AUDIT
    public String createdBy;
}
