package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Chris on 10/22/2016.
 */

public class QueryUtils {

    public static ArrayList<NewsStory> getNewsStories(String urlString) {

        URL url = createUrl(urlString);

        String jsonString = "";

        // Try to make request
        try {
            jsonString = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e("getNewsStories", "Could not make http request: ", e);
        }

        return extractFromJSON(jsonString);
    }

    private static URL createUrl(String urlString) {
        URL url;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e("createUrl", "Problem with URL string: ", e);
            return null;
        }

        return url;
    }

    private static ArrayList<NewsStory> extractFromJSON(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        ArrayList<NewsStory> resultArray = new ArrayList<NewsStory>();

        try {
            // Extract required fields
            JSONObject responseContainer = new JSONObject(jsonString);
            JSONObject responseObject = responseContainer.getJSONObject("response");

            JSONArray storiesArray = new JSONArray();
            if (responseObject.has("results")) {
                storiesArray = responseObject.getJSONArray("results");
            }

            // Fields needed
            String title = "";
            String author = "";
            String articleUrl = "";
            String datePublished = "";
            String thumbnailUrl = "";

            // Loop through results array to build up news stories
            for (int i = 0; i < storiesArray.length(); i++) {
                JSONObject currentStory = storiesArray.getJSONObject(i);

                if (currentStory.has("webTitle")) {
                    title = currentStory.getString("webTitle");
                }

                if (currentStory.has("webPublicationDate")) {
                    datePublished = currentStory.getString("webPublicationDate");
                }

                if (currentStory.has("webUrl")) {
                    articleUrl = currentStory.getString("webUrl");
                }

                if (currentStory.has("fields")) {
                    JSONObject fieldsObject = currentStory.getJSONObject("fields");

                    author = fieldsObject.getString("byline");
                    thumbnailUrl = fieldsObject.getString("thumbnail");
                }

                // Create news story object and add to result array
                resultArray.add(new NewsStory(title, datePublished, articleUrl, author, thumbnailUrl));
            }


        } catch (JSONException e) {
            Log.e("extractFromJSON", "Error parsing JSON: ", e);
        }

        return resultArray;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonString = "";

        if (url == null) {
            // Bad url form, return empty string
            return jsonString;
        }

        // Setup request
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            // Set up connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(10000);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {
                inputStream = urlConnection.getInputStream();
                jsonString = readFromStream(inputStream);
            }

        } catch (IOException e) {
            Log.e("makeHTTPRequest", "Error in request", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonString;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = reader.readLine()) != null) {
                resultStringBuilder.append(line);
            }
        }
        return resultStringBuilder.toString();
    }

}