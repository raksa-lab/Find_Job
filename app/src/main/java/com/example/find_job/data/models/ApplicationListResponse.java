package com.example.find_job.data.models;

import java.util.List;

public class ApplicationListResponse {

    public boolean success;
    public List<Application> applications;
    public Pagination pagination;
    public Summary summary;

    public static class Pagination {
        public int currentPage;
        public int totalPages;
        public int totalItems;
        public boolean hasNextPage;
        public boolean hasPrevPage;
        public int limit;
    }

    public static class Summary {
        public int total;
        public int pending;
        public int reviewed;
        public int shortlisted;
        public int interview;
        public int accepted;
        public int rejected;
        public int withdrawn;
    }
}
