package com.github.theopechli.zoomies;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        final twitter4j.Status status = (twitter4j.Status) ((ObjectWrapperForBinder) intent
                .getExtras().getBinder("status")).getObject();
    }
}