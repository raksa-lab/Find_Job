package com.example.find_job.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.find_job.R;
import com.example.find_job.services.AuthManager;
import com.example.find_job.services.FirestoreRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

/**
 * RegisterActivity
 * - Register new user (email & password)
 * - Send verification email
 * - Create Firestore profile under /users/{uid} with role "user"
 * - Sign out and navigate to LoginActivity
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPassword;
    private Button btnRegister;
    private final AuthManager authManager = new AuthManager();
    private final FirestoreRepository repo = new FirestoreRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register with Firebase Auth
            authManager.register(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get current user
                    FirebaseUser user = authManager.getCurrentUser();
                    if (user == null) {
                        Toast.makeText(this, "Registration succeeded but no user available", Toast.LENGTH_LONG).show();
                        return;
                    }

                    final String uid = user.getUid();

                    // Send verification email (optional but recommended)
                    user.sendEmailVerification()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Verification email sent. Please check your inbox.", Toast.LENGTH_LONG).show();
                            })
                            .addOnFailureListener(e -> {
                                // Not fatal — just inform
                                Toast.makeText(this, "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                    // Create user profile in Firestore
                    Map<String, Object> profile = new HashMap<>();
                    profile.put("uid", uid);
                    profile.put("name", name);
                    profile.put("email", email);
                    profile.put("role", "user");
                    profile.put("isActive", true);
                    profile.put("createdAt", FieldValue.serverTimestamp());

                    // Use repo.updateUser to set the doc (merges)
                    repo.updateUser(uid, profile)
                            .addOnSuccessListener(aVoid -> {
                                // After creating the profile, sign out and return to Login screen
                                authManager.signOut();
                                Toast.makeText(this, "Registration complete. Please login (verify your email first).", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                // Clear back stack so user cannot return to RegisterActivity via back button
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                // If Firestore write fails, you may want to remove the created auth user (optional).
                                Toast.makeText(this, "Failed to create profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            });

                } else {
                    String message = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
