package com.example.find_job.data.models;

import java.util.List;

public class UserProfile {
    public String uid;
    public String email;
    public String fullName;
    public String phone;
    public String location;
    public String role;
    public String bio;

    public List<String> skills;
    public List<Experience> experience;
    public List<Education> education;
}
