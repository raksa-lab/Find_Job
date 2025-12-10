package com.example.find_job.data.models;

import java.util.List;

public class JobResponse {
    public boolean success;
    public List<Job> jobs;
    public Pagination pagination;
    public class Pagination {
        public int total;
        public int page;
        public int limit;
    }

}