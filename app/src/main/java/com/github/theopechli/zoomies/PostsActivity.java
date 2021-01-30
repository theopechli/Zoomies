package com.github.theopechli.zoomies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

public class PostsActivity extends AppCompatActivity {

    private RecyclerView rvPosts;
    private RecyclerView.Adapter postsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<twitter4j.Status> postsList = new ArrayList<>();
    private int[] smnLogos = {R.drawable.ic_twitter, R.drawable.ic_facebook, R.drawable.ic_instagram};
    private TwitterInstance twitterInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Intent intent = getIntent();
        String hashtag = intent.getStringExtra("com.github.theopechli.zoomies.hashtag");

        if (DataHolder.getInstance().getTwitterInstance() == null) {
            twitterInstance = new TwitterInstance();
            DataHolder.getInstance().setTwitterInstance(twitterInstance);
        } else {
            twitterInstance = DataHolder.getInstance().getTwitterInstance();
        }

        rvPosts = findViewById(R.id.rvPosts);
        rvPosts.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(layoutManager);
        postsAdapter = new PostsAdapter(this, postsList, smnLogos);
        rvPosts.setAdapter(postsAdapter);

        new SearchPostsTask().execute(hashtag);
    }

    private class SearchPostsTask extends AsyncTask<String, Void, List<Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(String... params) {
            List<twitter4j.Status> tweets = new ArrayList<>();
            try {
                Query query = new Query(params[0]);
                QueryResult result;
                result = twitterInstance.getTwitter().search(query);
                if (result != null) {
                    tweets = result.getTweets();
                }
            } catch (TwitterException te) {
                te.printStackTrace();
            }
            return tweets;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> tweets) {
            if (!tweets.isEmpty()) {
                postsList.addAll(tweets);
                postsAdapter.notifyDataSetChanged();
            }
        }
    }
}