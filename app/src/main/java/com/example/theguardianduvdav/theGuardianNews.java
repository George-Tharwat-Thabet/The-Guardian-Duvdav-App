package com.example.theguardianduvdav;

import android.net.Uri;
import android.widget.ImageView;

public class theGuardianNews {

    //This is variables for inputs in listView
    private String DateInput;
    private String AuthorInput;
    private String TitleInput;
    private String NewsImageInput;
    private String UrlInput;

    //This is constructor for theGuardianNews Class
    public theGuardianNews(String dateInput, String authorInput, String titleInput, String newsImageInput, String urlInput) {
        this.DateInput = dateInput;
        this.AuthorInput = authorInput;
        this.TitleInput = titleInput;
        this.NewsImageInput = newsImageInput;
        this.UrlInput = urlInput;
    }

    // this is getters of variables that stored in it listView inputs
    public String getDateInput() {
        return DateInput;
    }

    public String getAuthorInput() {
        return AuthorInput;
    }

    public String getTitleInput() {
        return TitleInput;
    }

    public String getNewsImageInput() { return NewsImageInput; }

    public String getUrlInput() { return UrlInput; }

}