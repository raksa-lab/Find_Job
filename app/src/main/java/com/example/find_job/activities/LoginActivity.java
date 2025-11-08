package com.example.find_job.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // 1. IMPORT ADDED
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.find_job.R;
import com.example.find_job.services.AuthManager;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView btnForgot; // 2. TYPE CHANGED FROM Button to TextView
    private AuthManager authManager = new AuthManager();

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_login);

        // Initialization of views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnForgot = findViewById(R.id.btnForgot); // This assignment is now correct and will not crash

        // Login button listener
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            authManager.signIn(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Navigate to MainActivity on successful login
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    // Show error message on failure
                    String errorMessage = "Login failed.";
                    if (task.getException() != null) {
                        errorMessage = task.getException().getMessage();
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });

        // Listeners for navigation buttons
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        btnForgot.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }
}
