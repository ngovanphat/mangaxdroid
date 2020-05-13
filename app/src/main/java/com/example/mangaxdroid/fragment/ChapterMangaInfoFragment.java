package com.example.mangaxdroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.CustomChapterListAdapter;
import com.example.mangaxdroid.object.Chapter;
import com.example.mangaxdroid.object.Manga;
import java.util.ArrayList;

public class ChapterMangaInfoFragment extends Fragment {
    ListView listView;
    ArrayList<Chapter> listChapter;
    Manga manga;

    public ChapterMangaInfoFragment(Manga manga) {
        this.manga = manga;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        connectContent();

        listView = (ListView) view.findViewById(R.id.listManga);
        listView.setAdapter(new CustomChapterListAdapter(view.getContext(), R.layout.chapter_list_custom_row, listChapter));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReadChapterActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void connectContent() {
        listChapter = new ArrayList<>();
        listChapter.add(new Chapter("Chapter 1", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 2", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 3", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 4", "01/01/2020", "123456"));
        listChapter.add(new Chapter("Chapter 5", "01/01/2020", "123456"));
    }
}
