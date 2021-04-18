package com.example.theguardianduvdav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<theGuardianNews>> {

    // make a url of the Guardian api
    public static final String TECH_NEWS_URL = "https://content.guardianapis.com/search?api-key=d85e9e18-f12d-4b77-b77e-cdebdb099690";

    // make a TextView variable for Empty View
    private TextView EmptyText;

    // make a ImageView variable for Empty View
    private ImageView EmptyImage;

    private TextView NoNewsText;

    private ImageView NoNewsImage;

    // make a listView to add and display news items to it
    private ListView listView;

    // import TheGuardianAdapter class to add the objects stored in it to ListView
    private TheGuardianAdapter ListNewsAdapter;

    // make a progressbar to display it at download time and hide it when it finishes downloading
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.News_Lists);

        EmptyText = findViewById(R.id.EmptyText_View);

        EmptyImage = findViewById(R.id.EmptyImage_View);

        loadingBar = findViewById(R.id.Loading_bar);

        NoNewsText = findViewById(R.id.No_News_Text);

        NoNewsImage = findViewById(R.id.No_News_Image);

        ListNewsAdapter = new TheGuardianAdapter(this, new ArrayList<theGuardianNews>());
        listView.setAdapter(ListNewsAdapter);

        // make a clicking on each Link on the list of News
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // make a getItem from theGuardianNews class
                theGuardianNews currentTheGuardianNews = ListNewsAdapter.getItem(i);

                // make a Uri method to pass into the Intent constructor)
                Uri TheGuardianNewsUriParsing = Uri.parse(currentTheGuardianNews.getUrlInput());

                // make a intent method to open news url
                Intent openUrlNews = new Intent(Intent.ACTION_VIEW, TheGuardianNewsUriParsing);

                // start Intent
                if (openUrlNews.resolveActivity(getPackageManager()) != null) {
                    startActivity(openUrlNews);
                }
            }
        });

        InternetChecking(LoaderManager.getInstance(this));

    }

    // Here checking the internet connection
    public void InternetChecking(LoaderManager inputLoader){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //check if Networking is connected or not
        if (networkInfo != null && networkInfo.isConnected()) {
            inputLoader.initLoader(1, null, this);

        } else {
            loadingBar.setVisibility(View.GONE);
            EmptyText.setText(R.string.No_Internet);
            EmptyImage.setImageResource(R.drawable.internetaccesserror);
        }
    }

    @NonNull
    @Override
    public Loader<List<theGuardianNews>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri uri = Uri.parse(TECH_NEWS_URL);
        Uri.Builder builder = uri.buildUpon();

        //adding appemd queries from TECH_NEWS_URL
        builder.appendQueryParameter("section", "technology");
        builder.appendQueryParameter("show-tags", "contributor");
        builder.appendQueryParameter("show-fields", "thumbnail");
        builder.appendQueryParameter("use-date", "last-modified");
        builder.appendQueryParameter("page-size", String.valueOf(30));

        return new TheGuardianLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<theGuardianNews>> loader, List<theGuardianNews> data) {
        loadingBar.setVisibility(View.GONE);

        EmptyText.setVisibility(View.GONE);

        EmptyImage.setVisibility(View.GONE);

        NoNewsText.setText(R.string.noNewsText);

        NoNewsImage.setImageResource(R.drawable.nonews);

        ListNewsAdapter.clear();

        //make if statement data is not null will show all data
        if (data != null && !data.isEmpty()) {
            ListNewsAdapter.addAll(data);
            NoNewsText.setVisibility(View.GONE);
            NoNewsImage.setVisibility(View.GONE);
            loadingBar.clearFocus();
        }
        //make else if statement data is null will show No News Page
        else if (data == null && data.isEmpty()) {
            NoNewsText.setVisibility(View.VISIBLE);
            NoNewsImage.setVisibility(View.VISIBLE);
        }
    }

    @NonNull

    @Override
    public void onLoaderReset(@NonNull Loader<List<theGuardianNews>> loader) {
        ListNewsAdapter.clear();
    }

}