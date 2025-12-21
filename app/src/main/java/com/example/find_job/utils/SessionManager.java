package com.example.find_job.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "USER_SESSION";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ===============================
    // SAVE SESSION
    // ===============================
    public void saveSession(String token, int userId, String role) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_ROLE, role)
                .apply();
    }

    // ===============================
    // GETTERS
    // ===============================
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "");
    }

    // ===============================
    // ROLE CHECKS
    // ===============================
    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(getRole());
    }

    public boolean isJobSeeker() {
        return "seeker".equalsIgnoreCase(getRole());
    }

    // ===============================
    // LOGOUT
    // ===============================
    public void logout() {
        prefs.edit().clear().apply();
    }
}
