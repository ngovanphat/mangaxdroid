package com.example.mangaxdroid.activity.useractivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.object.Manga;
import java.util.ArrayList;

public class UserFavoriteListActivity extends AppCompatActivity {
    private ArrayList<Manga> favoriteMangas = new ArrayList<>();
    MangaAdapter adapter;
    ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        listView = (ListView) findViewById(R.id.listFavorites);
        toolbar = (Toolbar) findViewById(R.id.toolBarFavorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Manga manga = (Manga) bundle.getSerializable("manga");
        favoriteMangas.add(manga);

        adapter = new MangaAdapter(this, R.layout.manga_avatar, favoriteMangas);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
