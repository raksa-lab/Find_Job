package com.example.find_job.Auth;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.find_job.R;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword;
    Button btnRegister;
    TextView tvLoginInstead;

    RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLoginInstead = findViewById(R.id.tvLoginInstead);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        btnRegister.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.register(name, email, password).observe(this, response -> {

                if (response == null || response.uid == null) {
                    Toast.makeText(this, "Registration failed!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                finish();   // return to Login page
            });
        });

        tvLoginInstead.setOnClickListener(v -> finish());
    }
}
