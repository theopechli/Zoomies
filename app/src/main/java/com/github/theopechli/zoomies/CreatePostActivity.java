package com.github.theopechli.zoomies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

public class CreatePostActivity extends AppCompatActivity {

    private TwitterInstance twitterInstance;
    private static int REQUEST_CODE = 0;

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

        Button uploadMedia = findViewById(R.id.btnUploadMedia);
        uploadMedia.setOnClickListener(v -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, REQUEST_CODE);
        });

        Button createPost = findViewById(R.id.btnCreatePost);
        createPost.setOnClickListener(v -> {
            EditText etCreatePost = findViewById(R.id.etCreatePost);
            new CreatePostTask().execute(etCreatePost.getText().toString());
            etCreatePost.setText("");
        });
    }

    private class UploadMediaTask extends AsyncTask<Uri, Void, Void> {

        @Override
        protected Void doInBackground(Uri... uris) {
            UploadedMedia uploadedMedia = null;

            final String docId = DocumentsContract.getDocumentId(uris[0]);
            final String[] split = docId.split(":");
            final String type = split[0];
            String filePath = "";

            if ("primary".equalsIgnoreCase(type)) {
                filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
            }

            File file = new File(filePath);

            try {
                uploadedMedia = twitterInstance.getTwitter().uploadMediaChunked(file.getName()
                        , new BufferedInputStream(new FileInputStream(file)));
            } catch (TwitterException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            StatusUpdate statusUpdate = new StatusUpdate("test upload video");
            statusUpdate.setMediaIds(uploadedMedia.getMediaId());
            try {
                twitterInstance.getTwitter().updateStatus(statusUpdate);
            } catch (
                    TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK) {
            return;
        }

        new UploadMediaTask().execute(data.getData());
    }
}