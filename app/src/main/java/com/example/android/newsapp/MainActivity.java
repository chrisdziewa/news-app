package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>> {

    // Permanent url
    public static final String API_URL = "http://content.guardianapis.com/search?section=world&show-fields=byline%2Cbody%2Cthumbnail&api-key=test";

    private NewsRecycler mAdapter;
    private ProgressBar mProgressBar;
    private Timer mUpdateTimer;
    private TextView mNoConnectionTextView;
    private LoaderManager mLoaderManager;

    private Handler mUpdateDataHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsList = (RecyclerView) findViewById(R.id.recyclerView);
        newsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mNoConnectionTextView = (TextView) findViewById(R.id.no_connection_bubble);
        mNoConnectionTextView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        ArrayList<NewsStory> newsArray = new ArrayList<NewsStory>();

        mAdapter = new NewsRecycler(MainActivity.this, newsArray);

        newsList.setAdapter(mAdapter);

        mLoaderManager = getLoaderManager();

        if (hasConnection()) {
            mLoaderManager.restartLoader(0, null, MainActivity.this);
        } else {
            mNoConnectionTextView.setText(R.string.no_connection);
            mNoConnectionTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public android.content.Loader<List<NewsStory>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, API_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<NewsStory>> loader, List<NewsStory> stories) {

        mProgressBar.setVisibility(View.GONE);
        if (stories == null) {

            if (!hasConnection()) {
                mNoConnectionTextView.setText(R.string.no_connection);
                mNoConnectionTextView.setVisibility(View.VISIBLE);
            } else {
                // Show message when there are no stories or no internet connection
                mNoConnectionTextView.setText(R.string.no_results_string);
                mNoConnectionTextView.setVisibility(View.VISIBLE);
            }

            return;
        }

        mNoConnectionTextView.setVisibility(View.GONE);
        mAdapter.clear();
        mAdapter.addAll(stories);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<NewsStory>> loader) {
        mAdapter.clear();
    }

    private boolean hasConnection() {
        ConnectivityManager connectionManager = (ConnectivityManager)
                MainActivity.this
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectionManager.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Clear handler
        mUpdateDataHandler.removeCallbacks(updateRunnable);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        mUpdateDataHandler.removeCallbacks(updateRunnable);
        updateDataTask();
    }

    // Regularly updates the data
    private void updateDataTask() {
        mLoaderManager.restartLoader(0, null, MainActivity.this);

        // Start handler to auto-update
        mUpdateDataHandler.post(updateRunnable);
    }

    // Runnable task that calls AsyncLoader to get news stories
    final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (hasConnection()) {
                mNoConnectionTextView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                Log.i("UpdateRunnable", "Refreshing data");
                // Call again after 10 minutes
                mUpdateDataHandler.postDelayed(updateRunnable, 600000);
                mLoaderManager.restartLoader(0, null, MainActivity.this);
            } else {
                mNoConnectionTextView.setText(R.string.no_connection);
                mNoConnectionTextView.setVisibility(View.VISIBLE);
                // Connection not found, try again in 10 seconds
                mUpdateDataHandler.postDelayed(updateRunnable, 10000);
            }
        }
    };
}