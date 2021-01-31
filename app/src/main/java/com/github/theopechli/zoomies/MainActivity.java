package com.github.theopechli.zoomies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCreatePostActivity = findViewById(R.id.btnCreatePostActivity);
        btnCreatePostActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivity(intent);
        });

        Button btnHashtagsActivity = findViewById(R.id.btnHashtagsActivity);
        btnHashtagsActivity.setOnClickListener(v -> {
            Intent intent = new Intent(this, HashtagsActivity.class);
            startActivity(intent);
        });
    }
}