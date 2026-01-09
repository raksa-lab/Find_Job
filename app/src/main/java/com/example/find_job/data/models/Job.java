package com.example.find_job.data.models;

import java.util.ArrayList;
import java.util.List;

public class Job {

    public String id;
    public String title;
    public String description;
    public String company;
    public String location;

    public String type;
    public String employmentType;
    public String experienceLevel;
    public String category;
    public boolean remote;

    public int salary;

    public List<String> requirements = new ArrayList<>();
    public List<String> benefits = new ArrayList<>();
    public List<String> skills = new ArrayList<>();
    public List<String> tags = new ArrayList<>();

    public String companyLogo; // ✅ MISSING FIELD (CRITICAL)

    public String createdBy;
    public int applicantsCount;
    public int views;
    public String status;

    public CreatedAt createdAt;
    public CreatedAt updatedAt;

    public static class CreatedAt {
        public long _seconds;
        public long _nanoseconds;
    }
}

