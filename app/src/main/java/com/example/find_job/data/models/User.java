package com.example.find_job.data.models;

import java.util.List;

public class User {
    public String id;
    public String email;
    public String fullName;
    public String phone;
    public String role;
    public String location;
    public List<String> skills;
    public int applicationsCount;
    public Stats stats;

    public static class Stats {
        public int applications;
        public int favorites;
        public boolean profileComplete;
    }
}
