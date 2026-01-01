package com.example.find_job.data.models;

public class AdminApplication {

    public String id;
    public String stage;
    public String status;
    public String userName;
    public String userEmail;
    public String jobTitle;
    public String jobCompany;

    public Job job;

    public static class Job {
        public String id;
        public String title;
        public String company;
        public String location;
        public String status;
    }
}
