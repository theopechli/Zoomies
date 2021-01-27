package com.github.theopechli.zoomies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;

public class HashtagsActivity extends AppCompatActivity {

    private final String TAG = "HashtagsActivity";

    RecyclerView recyclerView;
    RecyclerView.Adapter trendsAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> trendsList = new ArrayList<>();
    int[] smnLogos = {R.drawable.ic_twitter, R.drawable.ic_facebook, R.drawable.ic_instagram};
    TwitterInstance twitter = new TwitterInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtags);

        recyclerView = findViewById(R.id.rvTrends);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        trendsAdapter = new TrendsAdapter(this, trendsList, smnLogos);
        recyclerView.setAdapter(trendsAdapter);

        final Button btnGetTrends = findViewById(R.id.btnGetTrends);
        btnGetTrends.setOnClickListener(v -> {
            // FIXME remove deprecated AsyncTask
            new GetTrendsTask(trendsList).execute();
        });

        final Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            EditText etQuery = findViewById(R.id.etQuery);
            String query;
            query = etQuery.getText().toString();
            new SearchHashtagTask().execute(query);
        });
    }

    private class SearchHashtagTask extends AsyncTask<String, Void, List<twitter4j.Status>> {

        @Override
        protected List<twitter4j.Status> doInBackground(String... params) {
            Log.i(TAG, "SearchHashtagTask doInBackground start.");

            List<twitter4j.Status> tweets = null;

            try {
                Query query = new Query(params[0]);
                QueryResult result;
                result = twitter.getTwitterInstance().search(query);
                tweets = result.getTweets();
            } catch (TwitterException te) {
                te.printStackTrace();
            }

            Log.i(TAG, "SearchHashtagTask doInBackground done.");

            return tweets;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> tweets) {
            Log.i(TAG, "onPostExecute done.");
        }
    }

    private class GetTrendsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private WeakReference<ArrayList<String>> mTrendsList;

        GetTrendsTask(ArrayList<String> arrayList) {
            mTrendsList = new WeakReference<>(arrayList);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            Log.i(TAG, "GetTrendsTask doInBackground start.");

            ArrayList<String> arrayList = new ArrayList<>();

            try {
                int woeid = 1;
                Trends trends = twitter.getTwitterInstance().getPlaceTrends(woeid);

                Log.i(TAG, "Showing trends for " + trends.getLocation().getName());

                for (Trend trend : trends.getTrends()) {
                    arrayList.add(trend.getName());
                }
            } catch (TwitterException te) {
                te.printStackTrace();
                Log.i(TAG, "Failed to get trends: " + te.getMessage());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                Log.i(TAG, "WOEID must be number");
            }

            Log.i(TAG, "GetTrendsTask doInBackground done.");

            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (result != null) {
                mTrendsList.get().addAll(result);
            }

            trendsAdapter.notifyDataSetChanged();

            Log.i(TAG, "GetTrendsTask onPostExecute done.");
        }
    }
}