package com.example.theguardianduvdav;

import android.nfc.Tag;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AttachFiles {

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("AttachFiles.java", "Problem when building the URL of The Guardian News ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection connectToUrl = null;
        InputStream inputStream = null;
        try {
            connectToUrl = (HttpURLConnection) url.openConnection();
            connectToUrl.setReadTimeout(10000 /* milliseconds */);
            connectToUrl.setConnectTimeout(15000 /* milliseconds */);
            connectToUrl.setRequestMethod("GET");
            connectToUrl.connect();

            if (connectToUrl.getResponseCode() == 200) {
                inputStream = connectToUrl.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("AttachFiles", "Error response code: " + connectToUrl.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("AttachFiles", "Problem retrieving The Guardian News JSON results.", e);
        } finally {
            if (connectToUrl != null) {
                connectToUrl.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public AttachFiles() {
    }

    public static List<theGuardianNews> theGuardianNewsExtractor(String newsInput){

        if (TextUtils.isEmpty(newsInput)) {
            return null;
        }

        //Bring the keys to the Guardian api newspaper
        //In JSON format
        List<theGuardianNews> news = new ArrayList<>();
        try {
            JSONObject jsonObjectInput = new JSONObject(newsInput);
            JSONObject jsonObjectResults = jsonObjectInput.getJSONObject("response");
            JSONArray jsonArrayResults = jsonObjectResults.getJSONArray("results");

            for (int L = 0; L < jsonArrayResults.length(); L++) {
                JSONObject currentTheGuardianNews = jsonArrayResults.getJSONObject(L);
                String Date = currentTheGuardianNews.getString("webPublicationDate");

                JSONArray AuthorArray = currentTheGuardianNews.getJSONArray("tags");
                String Author = "";
                for (int A = 0; A < AuthorArray.length(); A++){
                    Author = AuthorArray.getJSONObject(0).getString("webTitle");
                }

                JSONObject ImageObject = currentTheGuardianNews.getJSONObject("fields");
                String NewsImage = "";
                for(int I = 0; I < ImageObject.length(); I++){
                    NewsImage = ImageObject.getString("thumbnail");
                }

                String Title = currentTheGuardianNews.getString("webTitle");
                String UrlWeb = currentTheGuardianNews.getString("webUrl");

                theGuardianNews newsAdding = new theGuardianNews(Date, Author, Title, NewsImage, UrlWeb);
                news.add(newsAdding);
            }

        } catch (JSONException e) {
            Log.v("AttachFiles", "theGuardianNewsExtractor: Error in Json Parsing", e);
        }
        return news;
    }

    public static List<theGuardianNews> fetchingTheGuardianNews(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("AttachFiles", "Problem when making the HTTP request.", e);
        }

        return theGuardianNewsExtractor(jsonResponse);
    }
}
