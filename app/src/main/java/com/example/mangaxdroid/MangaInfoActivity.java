package com.example.mangaxdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MangaInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_info);
    }

    public void readFromStart(View view) {
        Intent intent = new Intent(MangaInfoActivity.this, ReadChapterActivity.class);
        startActivity(intent);
    }
}
