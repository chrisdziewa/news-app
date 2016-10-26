package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 10/20/2016.
 * Uses RecyclerView
 */

// Adapter class with recycler view
public class NewsRecycler extends RecyclerView.Adapter<NewsStoryHolder> {
    private List<NewsStory> mNewsStories;
    private Context mContext;
    private RecyclerView mNewsStoryRecyclerView;

    public NewsRecycler(Context context, List<NewsStory> newsStories) {
        mContext = context;
        mNewsStories = newsStories;
    }

    @Override
    public NewsStoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);

        return new NewsStoryHolder(view);
    }

    @Override
    public int getItemCount() {
        return mNewsStories.size();
    }

    @Override
    public void onBindViewHolder(NewsStoryHolder holder, int position) {
        NewsStory newsStory = mNewsStories.get(position);
        holder.setContext(mContext);
        holder.bindNewsStory(newsStory);
    }

    // add all items to recycler
    public void addAll(List<NewsStory> newsStories) {
        mNewsStories = newsStories;
        notifyDataSetChanged();
    }
    // Clear the adapter data
    public void clear() {
        mNewsStories = new ArrayList<NewsStory>();
        notifyDataSetChanged();
    }

}
