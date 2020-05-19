package com.example.mangaxdroid.activity.useractivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.object.Manga;
import java.util.ArrayList;

public class UserFavoriteListActivity extends Activity {
    private ArrayList<Manga> favoriteMangas = new ArrayList<>();
    MangaAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        listView = (ListView) findViewById(R.id.listFavorites);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Manga manga = (Manga) bundle.getSerializable("manga");
        favoriteMangas.add(manga);

        adapter = new MangaAdapter(this, R.layout.manga_avatar, favoriteMangas);
        listView.setAdapter(adapter);
    }
}
