package com.github.theopechli.zoomies;

import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterInstance {

    private Twitter twitter;

    public TwitterInstance() {
        // FIXME use different configuration for the twitter api
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("")
                .setOAuthConsumerSecret("")
                .setOAuthAccessToken("")
                .setOAuthAccessTokenSecret("");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();
    }

    public Twitter getTwitterInstance() {
        return twitter;
    }

    class GetTrendsTask extends AsyncTask<Void, Void, ArrayList<String>> {

        private RecyclerView.Adapter trendsAdapter;
        private WeakReference<ArrayList<String>> mTrendsList;

        GetTrendsTask(RecyclerView.Adapter adapter, ArrayList<String> arrayList) {
            trendsAdapter = adapter;
            mTrendsList = new WeakReference<>(arrayList);
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            ArrayList<String> arrayList = new ArrayList<>();

            try {
                int woeid = 1;
                Trends trends = twitter.getPlaceTrends(woeid);

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
            if (result != null) {
                mTrendsList.get().addAll(result);
            }

            trendsAdapter.notifyDataSetChanged();
        }
    }
}
