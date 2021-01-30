package com.github.theopechli.zoomies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.TwitterException;

public class HashtagsActivity extends AppCompatActivity {

    private RecyclerView rvTrends;
    private RecyclerView.Adapter trendsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> trendsList = new ArrayList<>();
    private int twitterLogo = R.drawable.ic_twitter;
    private TwitterInstance twitterInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtags);

        if (DataHolder.getInstance().getTwitterInstance() == null) {
            twitterInstance = new TwitterInstance();
            DataHolder.getInstance().setTwitterInstance(twitterInstance);
        }
        else {
            twitterInstance = DataHolder.getInstance().getTwitterInstance();
        }

        rvTrends = findViewById(R.id.rvTrends);
        rvTrends.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        rvTrends.setLayoutManager(layoutManager);
        trendsAdapter = new TrendsAdapter(this, trendsList, twitterLogo);
        rvTrends.setAdapter(trendsAdapter);

        final Button btnGetTrends = findViewById(R.id.btnGetTrends);
        btnGetTrends.setOnClickListener(v -> {
            // FIXME remove deprecated AsyncTask
            new GetTrendsTask().execute();
        });

        final Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(v -> {
            EditText etQuery = findViewById(R.id.etQuery);
            String query;
            query = etQuery.getText().toString();
            // TODO search twitter hashtags, not posts
        });
    }

    public class GetTrendsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> arrayList = new ArrayList<>();

            try {
                int woeid = 23424833;
                Trends trends = twitterInstance.getTwitter().getPlaceTrends(woeid);

                for (Trend trend : trends.getTrends()) {
                    arrayList.add(trend.getName());
                }
            } catch (TwitterException te) {
                te.printStackTrace();
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            if (!result.isEmpty()) {
                trendsList.addAll(result);
                trendsAdapter.notifyDataSetChanged();
            }
        }
    }
}