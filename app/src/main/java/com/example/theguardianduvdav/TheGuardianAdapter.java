package com.example.theguardianduvdav;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

public class TheGuardianAdapter extends ArrayAdapter<theGuardianNews> {

    public TheGuardianAdapter(Context context, List<theGuardianNews> theGuardianNews) {
        super(context, 0, theGuardianNews);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.guardian_list, parent, false);
        }

        TextView Date = (TextView) convertView.findViewById(R.id.Date_Id);
        TextView Author = (TextView) convertView.findViewById(R.id.Author_Id);
        TextView Title = (TextView) convertView.findViewById(R.id.Title_Id);
        ImageView Image = (ImageView) convertView.findViewById(R.id.News_Image);

        theGuardianNews currentTheGuardianNews = getItem(position);

        Date.setText(currentTheGuardianNews.getDateInput());
        Author.setText(currentTheGuardianNews.getAuthorInput());
        Title.setText(currentTheGuardianNews.getTitleInput());
        Picasso.get().load(currentTheGuardianNews.getNewsImageInput()).into(Image);
        return convertView;
    }
}
