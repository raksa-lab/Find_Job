package com.example.find_job.data.models;

public class LoginResponse {

    public boolean success;

    // 🔑 TOKENS
    public String idToken;
    public String refreshToken;
    public String expiresIn;

    // USER ID
    public String uid;

    // USER OBJECT
    public User user;

    public static class User {
        public String uid;
        public String email;
        public String fullName;
        public String role; // admin | seeker
    }
}
