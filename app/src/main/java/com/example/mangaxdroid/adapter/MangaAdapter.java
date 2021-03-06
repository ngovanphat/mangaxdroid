package com.example.mangaxdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class MangaAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Manga> listManga;
    private ArrayList<String> listChapter = new ArrayList<>();

    public MangaAdapter(Context context, int layout, ArrayList<Manga> listManga) {
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
        return 0;
    }
    public String getItemID(int position){
        return listManga.get(position).getId();
    }
    private class ViewHolder {
        ImageView imgManga;
        TextView nameManga;
        TextView categoryManga;
        TextView viewCount;
        TextView chapters;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.imgManga = (ImageView) convertView.findViewById(R.id.imageviewMangaAvatar);
            holder.nameManga = (TextView) convertView.findViewById(R.id.textviewMangaNameAvatar);
            holder.categoryManga = (TextView) convertView.findViewById(R.id.textviewCategoryMangaAvatar);
            holder.viewCount = (TextView) convertView.findViewById(R.id.textviewViewCount);
            //holder.chapters = (TextView) convertView.findViewById(R.id.textviewChapterMangaAvatar);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Manga manga = listManga.get(position);
        Glide.with(convertView)
                .load(manga.getImage())
                .centerCrop()
                .into(holder.imgManga);
        holder.imgManga.setClipToOutline(true);
        holder.nameManga.setText(manga.getName());
//        holder.categoryManga.setText(manga.getCategory());
        holder.viewCount.setText(String.valueOf(manga.getViewCount()));

        listChapter.clear();
        final String path = "Data/Chapters/" + manga.getName().toUpperCase();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(path);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    listChapter.add(data.getRef().getKey());
                }
                Collections.reverse(listChapter);
                if(!listChapter.isEmpty())
                holder.categoryManga.setText("Chương mới: " + listChapter.get(0));
                else{
                    holder.categoryManga.setText("Truyện này sẽ được cập nhật");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        return convertView;
    }
}
