package com.example.find_job.Auth;

import com.example.find_job.MainActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.find_job.R;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegisterNow;

    LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);

        // Setup ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Login button click
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Field validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call ViewModel method
            viewModel.loginUser(email, password).observe(this, response -> {

                if (response == null) {
                    Toast.makeText(this, "Invalid credentials or network error", Toast.LENGTH_SHORT).show();
                    return;
                }

                // SUCCESS
                Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();

                // TODO: Save user token here
                // SharedPreferences prefs = getSharedPreferences("AUTH", MODE_PRIVATE);
                // prefs.edit().putString("token", response.token).apply();

                // Navigate to MainActivity
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish(); // Important: Prevent back to login
            });
        });

        // Move to Register Page
        tvRegisterNow.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}
