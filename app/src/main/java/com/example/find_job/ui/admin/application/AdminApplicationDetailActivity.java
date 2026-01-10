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
import java.io.InputStream;

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

        loadData();

        btnReply.setOnClickListener(v -> showReplyDialog());
    }

    // =====================================================
    // LOAD DATA
    // =====================================================
    private void loadData() {
        viewModel.load(applicationId)
                .observe(this, this::bindData);
    }

    // =====================================================
    // BIND DATA (FINAL & CORRECT)
    // =====================================================
    private void bindData(AdminApplication app) {
        if (app == null) return;

        // JOB TITLE
        tvJobTitle.setText(
                app.jobTitle != null
                        ? app.jobTitle
                        : app.job != null
                        ? app.job.title
                        : "—"
        );

        // STATUS
        tvStatus.setText(
                app.status != null
                        ? app.status.toUpperCase()
                        : "—"
        );

        // USER MESSAGE
        tvUserNote.setText(
                app.coverLetter != null && !app.coverLetter.trim().isEmpty()
                        ? app.coverLetter
                        : "No message from user"
        );

        // =========================
        // ✅ ADMIN REPLY (LATEST)
        // =========================
        String adminReply = app.getAdminReply();

        tvAdminReply.setText(
                adminReply != null && !adminReply.trim().isEmpty()
                        ? adminReply
                        : "No reply yet"
        );

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
                                    loadData(); // reload after reply
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
    // DOWNLOAD & OPEN CV
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

        client.newCall(request).enqueue(new okhttp3.Callback() {

            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(
                                AdminApplicationDetailActivity.this,
                                "Failed to download CV",
                                Toast.LENGTH_SHORT
                        ).show()
                );
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) {
                try {
                    File pdf = new File(
                            getCacheDir(),
                            "resume_" + System.currentTimeMillis() + ".pdf"
                    );

                    InputStream in = response.body().byteStream();
                    FileOutputStream out = new FileOutputStream(pdf);

                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = in.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }

                    out.close();
                    in.close();

                    openPdf(pdf);

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
