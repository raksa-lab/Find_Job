package com.example.find_job.ui.profile.cv;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.appbar.MaterialToolbar;

import com.example.find_job.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MyCvActivity extends AppCompatActivity {

    private TextView tvStatus;
    private Button btnUpload, btnDelete;
    private MyCvViewModel viewModel;

    private ActivityResultLauncher<Intent> filePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cv);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        tvStatus = findViewById(R.id.tv_status);
        btnUpload = findViewById(R.id.btn_upload);
        btnDelete = findViewById(R.id.btn_delete);

        viewModel = new ViewModelProvider(this).get(MyCvViewModel.class);

        checkResumeStatus();
        setupPicker();

        btnUpload.setOnClickListener(v -> openFilePicker());
        btnDelete.setOnClickListener(v -> deleteResume());
    }

    private void checkResumeStatus() {
        viewModel.hasResume().observe(this, has -> {
            if (has) {
                tvStatus.setText("✅ Resume uploaded");
                btnDelete.setVisibility(Button.VISIBLE);
            } else {
                tvStatus.setText("❌ No resume uploaded");
                btnDelete.setVisibility(Button.GONE);
            }
        });
    }

    private void setupPicker() {
        filePicker = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {

                        Uri uri = result.getData().getData();
                        if (uri == null) return;

                        String mimeType = getContentResolver().getType(uri);
                        if (mimeType == null) {
                            Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        File file = copyUriToFile(uri);
                        uploadResume(file, mimeType);
                    }
                }
        );
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "image/jpeg",
                "image/png"
        });
        filePicker.launch(intent);
    }

    private void uploadResume(File file, String mimeType) {
        viewModel.uploadResume(file, mimeType).observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Resume uploaded", Toast.LENGTH_SHORT).show();
                checkResumeStatus();
            } else {
                Toast.makeText(this, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteResume() {
        viewModel.deleteResume().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Resume deleted", Toast.LENGTH_SHORT).show();
                checkResumeStatus();
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File copyUriToFile(Uri uri) {
        try {
            InputStream input = getContentResolver().openInputStream(uri);
            File file = new File(getCacheDir(), getFileName(uri));
            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }

            output.close();
            input.close();
            return file;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            String name = index != -1 ? cursor.getString(index) : "resume";
            cursor.close();
            return name;
        }
        return "resume";
    }
}
