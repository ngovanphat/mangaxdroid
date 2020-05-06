package com.example.mangaxdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MangaInfoActivity extends Activity {
    ImageView managaInfoMainLogo;
    ImageView mangaInfoCategoryLogo;
    ListView listViewChapter;
    ArrayList<Chapter> listChapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_info);
        connectContent();

        listViewChapter = (ListView) findViewById(R.id.listViewChapter);
        listViewChapter.setAdapter(new CustomChapterListAdapter(this, R.layout.chapter_list_custom_row, listChapter));
        listViewChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MangaInfoActivity.this, ReadChapterActivity.class);
                startActivity(intent);
            }
        });

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

        listChapter = new ArrayList<>();
        listChapter.add(new Chapter("Chapter 1", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 2", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 3", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 4", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 5", "01/01/2020", "123456"));
    }
}
