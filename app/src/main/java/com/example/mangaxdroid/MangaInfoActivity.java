package com.example.mangaxdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MangaInfoActivity extends Activity {
    ImageView managaInfoMainLogo;
    ImageView mangaInfoCategoryLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_info);
        connectContent();

        managaInfoMainLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MangaInfoActivity.this, MainActivity.class));
            }
        });

        mangaInfoCategoryLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MangaInfoActivity.this, CategoriesActivity.class));
            }
        });
    }

    public void readFromStart(View view) {
        Intent intent = new Intent(MangaInfoActivity.this, ReadChapterActivity.class);
        startActivity(intent);
    }

    private void connectContent(){
        managaInfoMainLogo = (ImageView) findViewById(R.id.managaInfoMainLogo);
        mangaInfoCategoryLogo = (ImageView) findViewById(R.id.mangaInfoCategoryLogo);
    }
}
