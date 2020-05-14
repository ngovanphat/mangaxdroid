package com.example.mangaxdroid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.mangaxdroid.R;
import com.squareup.picasso.Picasso;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    ArrayList<String> imgURLs;

    public ChapterAdapter(Context context, int layout, ArrayList<String> imgURLs){
        this.context = context;
        this.layout = layout;
        this.imgURLs=imgURLs;
    }

    @Override
    public int getCount() {
        if(imgURLs.size()<1)
            return 0;
        else return imgURLs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout,null);
        ImageView imgv = (ImageView) convertView.findViewById(R.id.mangaPage);
        Picasso.get().load(imgURLs.get(position)).into(imgv);
        return convertView;
    }
}
