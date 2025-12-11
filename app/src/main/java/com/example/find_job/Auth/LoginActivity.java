package com.example.find_job.Auth;

import com.example.find_job.MainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

        // CHECK IF USER ALREADY LOGGED IN
        SharedPreferences prefs = getSharedPreferences("USER_SESSION", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("LOGGED_IN", false);

        if (isLoggedIn) {
            // Skip login and go straight to main UI
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        // User not logged in → show login UI
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterNow = findViewById(R.id.tvRegisterNow);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.loginUser(email, password).observe(this, response -> {

                if (response == null) {
                    Toast.makeText(this, "Invalid credentials or network error", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Login success!", Toast.LENGTH_SHORT).show();

                // SAVE LOGIN SESSION
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("LOGGED_IN", true);

                // Save token if available
                if (response.token != null) {
                    editor.putString("TOKEN", response.token);
                }

                // Save email for profile
                editor.putString("EMAIL", email);

                editor.apply();

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            });
        });

        tvRegisterNow.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }
}
