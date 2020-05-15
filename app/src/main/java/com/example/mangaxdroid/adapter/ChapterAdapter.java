package com.example.mangaxdroid.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

public class ChapterAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    int images[];
    List<String> imgURLs;

    public ChapterAdapter(Context context, int layout, List<String> imgURLs){
        this.context = context;
        this.layout = layout;
        this.imgURLs=imgURLs;
    }

    public ChapterAdapter(Context context, int layout, int[] imgs) {
        this.context = context;
        this.layout = layout;
        this.images = imgs;
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
        //imgv.setImageResource(images[position]);
        Log.d("imgsrc",imgURLs.get(position));
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress);
        Glide.with(convertView.getContext()).load("http://st.truyenchon.com/data/comics/180/the-childrens-teacher-mr-kwon.jpg").listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).fitCenter().into(imgv);

        return convertView;
    }
}
