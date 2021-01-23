package com.github.theopechli.zoomies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;

public class GetTrendsActivity extends AppCompatActivity {

    private final String TAG = "GetTrendsActivity";

    RecyclerView recyclerView;
    RecyclerView.Adapter trendsAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> trendsList = new ArrayList<>();
    int[] smnLogos = {R.drawable.ic_twitter, R.drawable.ic_facebook, R.drawable.ic_instagram};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_trends);

        Intent intent = getIntent();

        // FIXME remove deprecated AsyncTask
        new RetrieveTrendsTask(trendsList).execute();

        Log.i(TAG, "onCreate");
        for (int idx = 0; idx < trendsList.size(); idx++) {
            Log.i(TAG, trendsList.get(idx));
        }

        recyclerView = findViewById(R.id.rvTrends);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private class RetrieveTrendsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        // FIXME remove unnecessary reference
        private WeakReference<ArrayList<String>> mTrendsList;

        RetrieveTrendsTask(ArrayList<String> arrayList) {
            mTrendsList = new WeakReference<>(arrayList);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> arrayList = new ArrayList<>();

            try {
                int woeid = 1;
                TwitterInstance twitter = new TwitterInstance();
                Trends trends = twitter.getTwitterInstance().getPlaceTrends(woeid);
                Log.i(TAG, "Showing trends for " + trends.getLocation().getName());

                for (Trend trend : trends.getTrends()) {
                    arrayList.add(trend.getName());
                }

                Log.i(TAG, "doInBackground done.");

                return arrayList;
            } catch (TwitterException te) {
                te.printStackTrace();
                Log.i(TAG, "Failed to get trends: " + te.getMessage());
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
                Log.i(TAG, "WOEID must be number");
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            if (result != null) {
                mTrendsList.get().addAll(result);
            }
            trendsAdapter = new TrendsAdapter(GetTrendsActivity.this, trendsList, smnLogos);
            recyclerView.setAdapter(trendsAdapter);

            Log.i(TAG, "onPostExecute done.");
        }
    }
}