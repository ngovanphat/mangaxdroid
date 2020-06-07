package com.example.mangaxdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryAdapter extends ArrayAdapter<String> {
    private Context context;
    private int layout;
    HistoryAdapter adapter = this;
    private ArrayList<Manga> listManga;
    private ArrayList<String> chapter;
    private ArrayList<String> listChapter = new ArrayList<>();

    public HistoryAdapter(Context context, int layout, ArrayList<Manga> listManga, ArrayList<String> chapter) {
        super(context, layout, chapter);
        this.context = context;
        this.layout=layout;
        this.listManga = listManga;
        this.chapter = chapter;
    }

    @Override
    public int getCount() {
        return listManga.size();
    }

    @Override
    public String getItem(int position) {
        return null;
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
        TextView curChapter;
        TextView percent;
        TextView totalChapter;
        ImageView trash;
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
            holder.curChapter = (TextView) convertView.findViewById(R.id.textviewMangaCurrentChapter);
            holder.percent = (TextView) convertView.findViewById(R.id.textviewPercent);
            holder.totalChapter = (TextView) convertView.findViewById(R.id.textviewMangaTotalChapter);
            holder.trash = (ImageView) convertView.findViewById(R.id.trash);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Manga manga = listManga.get(position);
        Glide.with(convertView)
                .load(manga.getImage())
                .centerCrop()
                .into(holder.imgManga);
        holder.imgManga.setClipToOutline(true);
        holder.nameManga.setText(manga.getName());
      //  holder.curChapter.setText("Lần xem trước: \tChapter " + chapter.get(position));

        listChapter.clear();
        final String path = "Data/Chapters/" + manga.getName().toUpperCase();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(path);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    listChapter.add(data.getRef().getKey());
                }
//                Collections.reverse(listChapter);
//                int cur = Integer.parseInt(chapter.get(position));
//                int total = Integer.parseInt(listChapter.get(0));
//                int percent = (cur * 100) / total ;
//                holder.percent.setText("[" + percent + " %]");
                holder.totalChapter.setText("Cập nhật đến chapter " + listChapter.get(0));
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        holder.trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String id = manga.getId();
                final DatabaseReference hisdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("History");
                hisdb.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                             if (ds.getKey().equals(id)) {
                                 hisdb.child(ds.getKey()).removeValue();
                                 chapter.remove(position);
                                 adapter.notifyDataSetChanged();
                             }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                hisdb.onDisconnect();
                Toast.makeText(context, "Successfully removed! ", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
