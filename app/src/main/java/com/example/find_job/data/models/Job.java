package com.example.find_job.data.models;

import java.util.List;

public class Job {
    public String id;
    public String title;
    public String description;
    public String company;
    public String location;
    public int salary;
    public String employmentType;
    public List<String> tags;
    public String createdBy;
    public int applicantsCount;
    public int views;
    public String status;

    public CreatedAt createdAt;

    public static class CreatedAt {
        public long _seconds;
        public long _nanoseconds;
    }
}
