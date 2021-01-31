package com.github.theopechli.zoomies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import twitter4j.TwitterException;

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

        Button createPost = findViewById(R.id.btnCreatePost);
        createPost.setOnClickListener(v -> {
            EditText etCreatePost = findViewById(R.id.etCreatePost);
            new CreatePostTask().execute(etCreatePost.getText().toString());
            etCreatePost.setText("");
        });
    }

    private class CreatePostTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            try {
                twitterInstance.getTwitter().updateStatus(strings[0]);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}