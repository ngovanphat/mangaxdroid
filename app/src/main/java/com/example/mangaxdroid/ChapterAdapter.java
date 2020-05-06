package com.example.mangaxdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import android.widget.TextView;
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
        return images.length;
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
        imgv.setImageResource(images[position]);
        //Picasso.get().load(imgURLs.get(position)).into(imgv);
        return convertView;
    }
}
