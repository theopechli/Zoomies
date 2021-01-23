package com.github.theopechli.zoomies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HashtagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends);
    }

    public void getTrends(View view) {
        Intent intent = new Intent(this, GetTrendsActivity.class);
        startActivity(intent);
    }

    public void searchHashtags(View view) {
        Intent intent = new Intent(this, SearchHashtagsActivity.class);
        startActivity(intent);
    }
}