package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 10/20/2016.
 */

public class NewsArrayAdapter extends ArrayAdapter<NewsStory> {

    private ArrayList<NewsStory> mNewsStories;

    public NewsArrayAdapter(Context context, List<NewsStory> newsStories) {
        super(context, 0, newsStories);
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = (View) LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        // Get current story
        NewsStory currentStory = getItem(position);

        // Reference widgets
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView dateTextView = (TextView) view.findViewById(R.id.publicationDate);

        // Byline
        TextView bylineView = (TextView) view.findViewById(R.id.author);
        bylineView.setText("by " + currentStory.getAuthors());

        // Populate date in ui
        titleTextView.setText(currentStory.getTitle());
        dateTextView.setText(currentStory.getPublicationDate());

        // Handle Image

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        if (!TextUtils.isEmpty(currentStory.getThumbnailUrl())) {
            Picasso.with(getContext())
                    .load(currentStory.getThumbnailUrl())
                    .resize(320, 180)
                    .into(imageView);
        } else {
            imageView.setVisibility(View.GONE);
        }

        return view;
    }
}
