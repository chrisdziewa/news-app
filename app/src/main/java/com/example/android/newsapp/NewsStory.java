package com.example.android.newsapp;

/**
 * Created by Chris on 10/20/2016.
 */

public class NewsStory {

    private String mTitle;
    private String mPublicationDate;
    private String mUrl;
    private String mAuthors;
    private String mThumbnailUrl;

    /**
     * @param title           of the article
     * @param publicationDate when the article was published
     * @param url             link to the complete story
     * @param authors         byline of article
     * @param thumbnailUrl    url to article image
     */
    public NewsStory(String title, String publicationDate, String url, String authors, String thumbnailUrl) {
        mTitle = title;
        mPublicationDate = publicationDate;
        mUrl = url;
        mAuthors = authors;
        mThumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        mPublicationDate = publicationDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getAuthors() {
        return mAuthors;
    }

    public void setAuthors(String authors) {
        mAuthors = authors;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }
}
