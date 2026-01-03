package com.example.find_job.data.models;

public class RegisterResponse {

    private boolean success;
    private String message;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public static class User {
        private String uid;
        private String email;
        private String displayName;

        public String getUid() {
            return uid;
        }

        public String getEmail() {
            return email;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
