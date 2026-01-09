package com.example.find_job.ui.admin.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.example.find_job.R;
import com.example.find_job.data.models.AdminApplication;
import com.example.find_job.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminApplicationDetailActivity extends AppCompatActivity {

    // =========================
    // UI
    // =========================
    private TextView tvJobTitle;
    private TextView tvStatus;
    private TextView tvUserNote;
    private TextView tvAdminReply;

    private MaterialButton btnReply;
    private MaterialButton btnViewCv;

    // =========================
    // DATA
    // =========================
    private AdminApplicationDetailViewModel viewModel;
    private String applicationId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_application_detail);

        // =========================
        // BIND UI
        // =========================
        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvStatus = findViewById(R.id.tvStatus);
        tvUserNote = findViewById(R.id.tvUserNote);
        tvAdminReply = findViewById(R.id.tvAdminReply);

        btnReply = findViewById(R.id.btnReply);
        btnViewCv = findViewById(R.id.btnViewCv);

        applicationId = getIntent().getStringExtra("app_id");
        if (applicationId == null) {
            finish();
            return;
        }

        viewModel = new ViewModelProvider(this)
                .get(AdminApplicationDetailViewModel.class);

        viewModel.load(applicationId)
                .observe(this, this::bindData);

        btnReply.setOnClickListener(v -> showReplyDialog());
    }

    // =====================================================
    // BIND DATA
    // =====================================================
    private void bindData(AdminApplication app) {
        if (app == null) return;

        // JOB TITLE
        tvJobTitle.setText(
                app.jobTitle != null ? app.jobTitle : "—"
        );

        // STATUS
        tvStatus.setText(
                app.status != null
                        ? app.status.toUpperCase()
                        : "—"
        );

        // USER NOTE (coverLetter)
        if (app.coverLetter != null && !app.coverLetter.trim().isEmpty()) {
            tvUserNote.setText(app.coverLetter);
        } else {
            tvUserNote.setText("No message from user");
        }

        // ADMIN REPLY (SAFE OBJECT HANDLING)
        String adminReply = app.getAdditionalInfoText();
        if (adminReply != null && !adminReply.trim().isEmpty()) {
            tvAdminReply.setText(adminReply);
        } else {
            tvAdminReply.setText("No reply yet");
        }

        // VIEW CV
        if (app.resumeUrl != null && !app.resumeUrl.isEmpty()) {
            btnViewCv.setVisibility(View.VISIBLE);
            btnViewCv.setOnClickListener(v ->
                    downloadAndOpenPdf(app.resumeUrl)
            );
        } else {
            btnViewCv.setVisibility(View.GONE);
        }
    }

    // =====================================================
    // ADMIN REPLY DIALOG
    // =====================================================
    private void showReplyDialog() {

        EditText input = new EditText(this);
        input.setHint("Reply to user");

        new AlertDialog.Builder(this)
                .setTitle("Admin Reply")
                .setView(input)
                .setPositiveButton("Send", (d, w) -> {

                    String reply = input.getText().toString().trim();
                    if (reply.isEmpty()) {
                        Toast.makeText(
                                this,
                                "Reply cannot be empty",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }

                    viewModel.replyToUser(applicationId, reply)
                            .observe(this, success -> {
                                if (Boolean.TRUE.equals(success)) {
                                    Toast.makeText(
                                            this,
                                            "Reply sent",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                    // Reload data
                                    viewModel.load(applicationId);
                                } else {
                                    Toast.makeText(
                                            this,
                                            "Failed to send reply",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // =====================================================
    // DOWNLOAD CV WITH AUTH
    // =====================================================
    private void downloadAndOpenPdf(String resumePath) {

        String url =
                "https://backend-mobile-mad.vercel.app" + resumePath;

        SessionManager session = new SessionManager(this);
        String token = session.getToken();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(
                                AdminApplicationDetailActivity.this,
                                "Failed to download CV",
                                Toast.LENGTH_SHORT
                        ).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (!response.isSuccessful() || response.body() == null) {
                    runOnUiThread(() ->
                            Toast.makeText(
                                    AdminApplicationDetailActivity.this,
                                    "CV not found",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );
                    return;
                }

                try {
                    File pdfFile = new File(
                            getCacheDir(),
                            "resume_" + System.currentTimeMillis() + ".pdf"
                    );

                    InputStream inputStream =
                            response.body().byteStream();
                    FileOutputStream outputStream =
                            new FileOutputStream(pdfFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                    openPdf(pdfFile);

                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(
                                    AdminApplicationDetailActivity.this,
                                    "Unable to open CV",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );
                }
            }
        });
    }

    // =====================================================
    // OPEN PDF
    // =====================================================
    private void openPdf(File file) {

        Uri uri = FileProvider.getUriForFile(
                this,
                getPackageName() + ".provider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(intent, "Open CV"));
    }
}
