package com.example.mangaxdroid.adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mangaxdroid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class ChapterAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<String> imgURLs;
    private SharedPreferences pageCountSharedPref;

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
        pageCountSharedPref = context.getSharedPreferences("readPages", context.MODE_PRIVATE);
        final View finalConvertView = convertView;
        Picasso.get().load(imgURLs.get(position)).into(imgv, new Callback() {
            @Override
            public void onSuccess() {
                ProgressBar progressBar=(ProgressBar) finalConvertView.findViewById(R.id.progress);
                progressBar.setVisibility(View.GONE);
                SharedPreferences.Editor edit = pageCountSharedPref.edit();
                String curCount=pageCountSharedPref.getString("pageCount","0");
                edit.putString("pageCount", String.valueOf(Integer.parseInt(curCount)+1));
                edit.apply();
            }

            @Override
            public void onError(Exception e) {
                //TODO add retry button or error image here
                //.error(R.drawable.error_placeholder_image)
                //Button retry
            }
        });

        return convertView;
    }
}
