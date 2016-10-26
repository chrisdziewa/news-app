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
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsStory>> {

    // Permanent url
    public static final String API_URL = "http://content.guardianapis.com/search?section=world&show-fields=byline%2Cbody%2Cthumbnail&api-key=test";

    private NewsRecycler mAdapter;
    private TextView mEmptyTextView;
    private ProgressBar mProgressBar;
    private Timer mUpdateTimer;

    private LoaderManager mLoaderManager;

    private Handler mUpdateDataHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView newsList = (RecyclerView) findViewById(R.id.recyclerView);
        newsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        mEmptyTextView = (TextView) findViewById(R.id.empty_text_view);
        mEmptyTextView.setVisibility(View.GONE);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);

        ArrayList<NewsStory> newsArray = new ArrayList<NewsStory>();

        mAdapter = new NewsRecycler(MainActivity.this, newsArray);

        newsList.setAdapter(mAdapter);

        mLoaderManager = getLoaderManager();

        if (hasConnection()) {
            mLoaderManager.restartLoader(0, null, MainActivity.this);
        } else {
            mEmptyTextView.setText(R.string.no_connection);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public android.content.Loader<List<NewsStory>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(MainActivity.this, API_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<NewsStory>> loader, List<NewsStory> stories) {
        mAdapter.clear();
        mProgressBar.setVisibility(View.GONE);
        mEmptyTextView.setVisibility(View.GONE);

        if (stories == null) {
            // Show message when there are no stories or no internet connection
            mEmptyTextView.setText(R.string.no_results_string);
            mEmptyTextView.setVisibility(View.VISIBLE);
            return;
        }

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

    // Prevents thread exception when updating the UI
    // Used with TimerTask
    final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();

        updateDataTimer();
    }

    private void updateDataTimer() {
        // Constant updates every 2 minutes
        if (mUpdateTimer != null) {
            mUpdateTimer.cancel();
            mUpdateTimer.purge();
            mUpdateTimer = new Timer();
        } else {
            mUpdateTimer = new Timer();
        }
        mUpdateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (hasConnection()) {
                    Log.i("TimerTask", "Refreshing data");
                    mUpdateDataHandler.post(updateRunnable);
                    mLoaderManager.restartLoader(0, null, MainActivity.this);
                }
            }
        }, 0, 120000);
    }
}