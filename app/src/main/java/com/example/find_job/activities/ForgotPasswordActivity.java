package com.example.find_job.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// --- THIS IS THE FIX ---
// Add the import for your project's R class.
import com.example.find_job.R;

import com.example.find_job.services.AuthManager;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnSend;
    private AuthManager authManager = new AuthManager();

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        // This line will now work because 'R' can be found via the import.
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.emailField);
        btnSend = findViewById(R.id.resetButton);

        btnSend.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            authManager.sendPasswordReset(email).addOnCompleteListener(t -> {
                if (t.isSuccessful()) {
                    Toast.makeText(this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String errorMessage = "Failed to send reset link.";
                    if (t.getException() != null) {
                        errorMessage = t.getException().getMessage();
                    }
                    Toast.makeText(this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
