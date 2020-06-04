package com.example.mangaxdroid.adapter;

import android.content.Context;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.mangaxdroid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout,null);
        final ImageView imgv = (ImageView) convertView.findViewById(R.id.mangaPage);
        final ProgressBar progressBar=convertView.findViewById(R.id.progressBar);
        pageCountSharedPref = context.getSharedPreferences("readPages", context.MODE_PRIVATE);
        final View finalConvertView = convertView;
        finalConvertView.setTag("loading");
        final TextDrawable errorDrawable =
                TextDrawable.builder()
                        .beginConfig()
                            .textColor(Color.WHITE)
                            .fontSize(spToPx(14,context))
                            .height(Resources.getSystem().getDisplayMetrics().heightPixels/10)
                            .width(Resources.getSystem().getDisplayMetrics().widthPixels-dpToPx(20,context))
                            .bold()
                        .endConfig()
                .buildRoundRect("Error while loading page",Color.parseColor("#870000"),dpToPx(15,context));

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imgv.getLayoutParams();
        params.topMargin=Resources.getSystem().getDisplayMetrics().heightPixels*20/100;
        params.bottomMargin = Resources.getSystem().getDisplayMetrics().heightPixels*40/100;
        progressBar.setLayoutParams(params);

        if(isNetworkAvailable()) {
            Picasso.get().load(imgURLs.get(position))
                    .error(errorDrawable)
                    .into(imgv, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            imgv.setTag("loaded");
                            finalConvertView.setTag("loaded");
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imgv.getLayoutParams();
                            params.topMargin = 0;
                            params.bottomMargin = 0;
                            imgv.setLayoutParams(params);
                        }
                        @Override
                        public void onError(Exception e) {
                            progressBar.setVisibility(View.GONE);
                            imgv.setImageDrawable(errorDrawable);
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) imgv.getLayoutParams();
                            params.topMargin=Resources.getSystem().getDisplayMetrics().heightPixels*20/100;
                            params.bottomMargin = Resources.getSystem().getDisplayMetrics().heightPixels*40/100;
                            imgv.setLayoutParams(params);
                            finalConvertView.setTag("error");
                            //TODO fix error not triggered
                        }
                    });
        }else{
            //TODO offline load
        }
        return finalConvertView;
    }
    //https://stackoverflow.com/questions/29664993/how-to-convert-dp-px-sp-among-each-other-especially-dp-and-sp
    static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
    static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public int convertToDp(int input) {
        final float scale =context.getResources().getDisplayMetrics().density;
        return (int) (input * scale + 0.5f);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
