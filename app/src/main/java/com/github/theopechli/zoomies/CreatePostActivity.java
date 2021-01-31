package com.github.theopechli.zoomies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

public class CreatePostActivity extends AppCompatActivity {

    private TwitterInstance twitterInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        getIntent();

        if (DataHolder.getInstance().getTwitterInstance() == null) {
            twitterInstance = new TwitterInstance();
            DataHolder.getInstance().setTwitterInstance(twitterInstance);
        } else {
            twitterInstance = DataHolder.getInstance().getTwitterInstance();
        }


    }
}