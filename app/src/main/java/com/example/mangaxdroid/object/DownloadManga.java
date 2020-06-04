package com.example.mangaxdroid.object;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mangaxdroid.fragment.ReadHorizontalFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DownloadManga {
    Manga manga;
    String chapterID;
    Context context;
    ArrayList<String> imgURLs;

    public DownloadManga(Manga manga, String chapterID, Context context) {
        this.manga = manga;
        this.chapterID = chapterID;
        this.context = context;
    }

    public ArrayList<String> fetchChapter(String mangaName, final String chapterId){
        imgURLs = new ArrayList<>();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference().child("Data").child("Chapters").child(mangaName).child(chapterId).child("imageURL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    String url = data.getValue(String.class);
                    imgURLs.add(url);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return imgURLs;
    }



}
