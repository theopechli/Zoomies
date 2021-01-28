package com.github.theopechli.zoomies;

public class DataHolder {
    private TwitterInstance twitterInstance;

    public TwitterInstance getTwitterInstance() {
        return twitterInstance;
    }

    public void setTwitterInstance(TwitterInstance twitterInstance) {
        this.twitterInstance = twitterInstance;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }
}
