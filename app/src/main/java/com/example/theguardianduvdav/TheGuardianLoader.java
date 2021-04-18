package com.example.theguardianduvdav;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class TheGuardianLoader extends AsyncTaskLoader<List<theGuardianNews>> {

    public String PLAY_URL;

    public TheGuardianLoader(@NonNull Context context, String urlInputPlaying) {
        super(context);
        PLAY_URL = urlInputPlaying;
    }

    @Nullable
    @Override
    public List<theGuardianNews> loadInBackground() {
        if (PLAY_URL == null){
            return null;
        }

        return AttachFiles.fetchingTheGuardianNews(PLAY_URL);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
