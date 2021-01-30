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
import twitter4j.TwitterException;

public class PostDetailsActivity extends AppCompatActivity {

    private RecyclerView rvPostDetails;
    private RecyclerView.Adapter postRepliesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<twitter4j.Status> postRepliesList = new ArrayList<>();
    private int[] smnLogos = {R.drawable.ic_twitter, R.drawable.ic_facebook, R.drawable.ic_instagram};
    private TwitterInstance twitterInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        final twitter4j.Status post = (twitter4j.Status) ((ObjectWrapperForBinder) intent
                .getExtras().getBinder("status")).getObject();

        if (DataHolder.getInstance().getTwitterInstance() == null) {
            twitterInstance = new TwitterInstance();
            DataHolder.getInstance().setTwitterInstance(twitterInstance);
        } else {
            twitterInstance = DataHolder.getInstance().getTwitterInstance();
        }

        rvPostDetails = findViewById(R.id.rvPostDetails);
        rvPostDetails.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rvPostDetails.setLayoutManager(layoutManager);
        postRepliesAdapter = new PostRepliesAdapter(this, post, postRepliesList, smnLogos);
        rvPostDetails.setAdapter(postRepliesAdapter);

        new PostDetailsActivity.GetRepliesTask().execute(post);
    }

    private class GetRepliesTask extends AsyncTask<twitter4j.Status, Void, ArrayList<twitter4j.Status>> {

        @Override
        protected ArrayList<twitter4j.Status> doInBackground(twitter4j.Status... status) {
            ArrayList<twitter4j.Status> replies = new ArrayList<>();

            try {
                Query query = new Query("to:" + status[0].getUser().getScreenName()
                        + " since_id:" + status[0].getId());
                QueryResult results;

                do {
                    results = twitterInstance.getTwitter().search(query);
                    List<twitter4j.Status> tweets = results.getTweets();

                    for (twitter4j.Status tweet : tweets)
                        if (tweet.getInReplyToStatusId() == status[0].getId())
                            replies.add(tweet);
                } while ((query = results.nextQuery()) != null);
            } catch (TwitterException e) {
                e.printStackTrace();
            }

            return replies;
        }

        @Override
        protected void onPostExecute(ArrayList<twitter4j.Status> replies) {
            if (!replies.isEmpty()) {
                postRepliesList.addAll(replies);
                postRepliesAdapter.notifyDataSetChanged();
            }
        }
    }
}