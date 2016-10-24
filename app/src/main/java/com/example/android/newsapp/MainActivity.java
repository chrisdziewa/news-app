package com.example.android.newsapp;

import android.app.LoaderManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>> {

    // Permanent url
    public static final String API_URL = "http://content.guardianapis.com/search?section=world&show-fields=byline%2Cbody%2Cthumbnail&api-key=test";

    private NewsArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsList = (ListView) findViewById(R.id.news_list);

        // Temporary data
        ArrayList<NewsStory> newsArray = new ArrayList<NewsStory>();

        mAdapter = new NewsArrayAdapter(MainActivity.this, newsArray);

        newsList.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(0, null, MainActivity.this);
    }

    @Override
    public android.content.Loader<List<NewsStory>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, API_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<NewsStory>> loader, List<NewsStory> stories) {
        mAdapter.clear();

        if (stories == null) {
            // Show message when there are no stories or no internet connection
            Log.i("onLoadFinished", "There are no stories");
            return;
        }

        mAdapter.addAll(stories);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<NewsStory>> loader) {
        mAdapter.clear();
    }
}
