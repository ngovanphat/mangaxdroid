package com.example.mangaxdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class MangaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Manga> listManga;

    public MangaAdapter(Context context,int layout, ArrayList<Manga> listManga) {
        this.context = context;
        this.layout=layout;
        this.listManga = listManga;
    }

    @Override
    public int getCount() {
        return listManga.size();
    }

    @Override
    public Object getItem(int position) {
        return listManga.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listManga.get(position).getId();
    }

    private class ViewHolder{
        ImageView imgManga;
        TextView nameManga;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.imgManga = (ImageView) convertView.findViewById(R.id.imageviewMangaAvatar);
            holder.nameManga = (TextView) convertView.findViewById(R.id.textviewMangaAvatar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Manga manga = listManga.get(position);
        holder.imgManga.setImageResource(manga.getImage());
        holder.nameManga.setText(manga.getName());
        return convertView;
    }
}
