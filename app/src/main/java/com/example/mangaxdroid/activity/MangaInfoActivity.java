package com.example.mangaxdroid.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.useractivity.UserFavoriteListActivity;
import com.example.mangaxdroid.fragment.MangaInfoFragment;
import com.example.mangaxdroid.object.Manga;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MangaInfoActivity extends AppCompatActivity {
    ImageView image;
    TextView name;
    FrameLayout frame;
    Context context;
    BottomNavigationView navigationBarMangaInfo;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_info_new);
        context = this.getApplicationContext();
        connectContent();

        toolbar = findViewById(R.id.toolBarMangaInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Manga manga = (Manga) bundle.getSerializable("manga");

        name.setText(manga.getName());
        Glide.with(this)
                .load(manga.getImage())
                .fitCenter()
                .into(image);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MangaInfoFragment MIF = MangaInfoFragment.newInstance();
        transaction.replace(R.id.frameMangaInfo, MIF);
        transaction.commit();

        MIF.onMsgFromMainToFragment(manga);

        navigationBarMangaInfo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        Intent intent = new Intent(context, UserFavoriteListActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("manga", manga);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.action_recents:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void connectContent() {
        name = (TextView) findViewById(R.id.mangaTitleNew);
        image = (ImageView) findViewById(R.id.mangaThumbnailNew);
        frame = (FrameLayout) findViewById(R.id.frameMangaInfo);
        navigationBarMangaInfo = (BottomNavigationView) findViewById(R.id.navigationBarMangaInfo);
    }
}
