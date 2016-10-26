package com.example.android.newsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Chris on 10/25/2016.
 */

public class NewsStoryHolder extends RecyclerView.ViewHolder {
    // Set up view holder pattern
    private NewsStory mNewsStory;
    private TextView mDateView;
    private TextView mTitleView;
    private TextView mAuthorView;
    private ImageView mImageView;
    private Context mContext;

    public NewsStoryHolder(View itemView) {
        super(itemView);

        mDateView = (TextView) itemView.findViewById(R.id.publicationDate);
        mTitleView = (TextView) itemView.findViewById(R.id.title);
        mAuthorView = (TextView) itemView.findViewById(R.id.author);
        mImageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

    public void bindNewsStory(NewsStory newsStory) {
        mNewsStory = newsStory;

        mDateView.setText(mNewsStory.getPublicationDate());
        mTitleView.setText(mNewsStory.getTitle());
        mAuthorView.setText(mNewsStory.getAuthors());
        Picasso.with(mContext)
                .load(mNewsStory.getThumbnailUrl())
                .resize(320, 180)
                .into(mImageView);
    }

    // For use with Picasso
    public void setContext(Context context) {
        mContext = context;
    }
}

