package com.example.find_job.ui.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.find_job.Auth.LoginActivity;
import com.example.find_job.utils.SessionManager;

/**
 * Base activity for ADMIN-only screens.
 * Any activity that extends this class is protected automatically.
 */
public abstract class BaseAdminActivity extends BaseAuthActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = new SessionManager(this);

        // 🚫 Block non-admin users
        if (!sessionManager.isAdmin()) {
            redirectUnauthorized();
        }
    }

    /**
     * Redirect non-admin users safely
     */
    protected void redirectUnauthorized() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
