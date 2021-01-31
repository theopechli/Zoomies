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
    private twitter4j.StatusUpdate post;
    private static int REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Intent intent = getIntent();

        twitter4j.Status status = null;
        try {
            status = (twitter4j.Status) ((ObjectWrapperForBinder) intent
                    .getExtras().getBinder("status")).getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (DataHolder.getInstance().getTwitterInstance() == null) {
            twitterInstance = new TwitterInstance();
            DataHolder.getInstance().setTwitterInstance(twitterInstance);
        } else {
            twitterInstance = DataHolder.getInstance().getTwitterInstance();
        }

        Button uploadMedia = findViewById(R.id.btnUploadMedia);
        /*
         FIXME
         If called first, `StatusUpdate post` will be null. Thus, when
         UploadImageTask/UploadMediaTask is called, it will try to set attributes of a null object.
         */
        uploadMedia.setOnClickListener(v -> {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            // FIXME add explicit filetypes
            chooseFile.setType("*/*");
            chooseFile = Intent.createChooser(chooseFile, "Choose a file");
            startActivityForResult(chooseFile, REQUEST_CODE);
        });

        Button createPost = findViewById(R.id.btnCreatePost);
        twitter4j.Status finalStatus = status;
        createPost.setOnClickListener(v -> {
            EditText etCreatePost = findViewById(R.id.etCreatePost);

            if (finalStatus != null) {
                post = new StatusUpdate("@" + finalStatus.getUser().getScreenName() + " "
                        + etCreatePost.getText().toString());
                post.setInReplyToStatusId(finalStatus.getId());
            } else {
                post = new StatusUpdate(etCreatePost.getText().toString());
            }

            new CreatePostTask().execute(post);
            etCreatePost.setText("");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK) {
            return;
        }

        // FIXME media type unrecognized
        new UploadImageTask(post).execute(data.getData());
        // FIXME chunked upload failed
        // new UploadMediaTask(statusUpdate).execute(data.getData());
    }

    private class UploadMediaTask extends AsyncTask<Uri, Void, Void> {

        private StatusUpdate statusUpdate;

        public UploadMediaTask(StatusUpdate statusUpdate) {
            this.statusUpdate = statusUpdate;
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            UploadedMedia uploadedMedia = null;

            File file = new File(GetFile(uris[0]));

            try {
                uploadedMedia = twitterInstance.getTwitter().uploadMediaChunked(file.getName()
                        , new BufferedInputStream(new FileInputStream(file)));
            } catch (TwitterException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            statusUpdate.setMediaIds(uploadedMedia.getMediaId());

            return null;
        }
    }

    private class UploadImageTask extends AsyncTask<Uri, Void, Void> {

        private StatusUpdate statusUpdate;

        public UploadImageTask(StatusUpdate statusUpdate) {
            this.statusUpdate = statusUpdate;
        }

        @Override
        protected Void doInBackground(Uri... uris) {
            File image = new File(GetFile(uris[0]));

            statusUpdate.setMedia(image);

            return null;
        }
    }

    private class CreatePostTask extends AsyncTask<twitter4j.StatusUpdate, Void, Void> {

        @Override
        protected Void doInBackground(twitter4j.StatusUpdate... statusUpdates) {
            try {
                twitterInstance.getTwitter().updateStatus(statusUpdates[0]);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public String GetFile(Uri uri) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/" + split[1];
        }

        return null;
    }
}