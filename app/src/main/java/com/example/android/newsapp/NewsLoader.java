package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Chris on 10/22/2016.
 * Handles async loading of news stories using the supplied url
 */

class NewsLoader extends AsyncTaskLoader {
    private String mUrl;

    NewsLoader(Context context, String url) {
        super(context);

        mUrl = url;
    }

    @Override
    public ArrayList<NewsStory> loadInBackground() {
        return QueryUtils.getNewsStories(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
