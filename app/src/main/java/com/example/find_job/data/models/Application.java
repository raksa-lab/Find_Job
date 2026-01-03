package com.example.find_job.data.models;

public class Application {

    public String id;
    public String status;

    public String jobTitle;
    public String jobCompany;

    public Job job;

    public static class Job {
        public String id;
        public String title;
        public String company;
        public String location;
        public String type;
        public String status;
    }
}
