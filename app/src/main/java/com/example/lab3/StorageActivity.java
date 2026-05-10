package com.example.lab3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StorageActivity extends AppCompatActivity {

    private static final String FILE_NAME = "book_results.txt";

    private TextView storageTextView;
    private Button backButton;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        storageTextView = findViewById(R.id.storageTextView);
        backButton = findViewById(R.id.backButton);
        clearButton = findViewById(R.id.clearButton);

        showSavedData();

        backButton.setOnClickListener(v -> finish());

        clearButton.setOnClickListener(v -> {
            deleteFile(FILE_NAME);
            showSavedData();
        });
    }

    private void showSavedData() {
        String data = readFileData();

        if (data.trim().isEmpty()) {
            storageTextView.setText("Сховище порожнє.\nЗбережені дані відсутні.");
        } else {
            storageTextView.setText(data);
        }
    }

    private String readFileData() {
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            byte[] bytes = new byte[fis.available()];
            int readBytes = fis.read(bytes);

            if (readBytes <= 0) {
                return "";
            }

            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }
}