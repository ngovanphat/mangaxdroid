package com.example.mangaxdroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.CustomChapterListAdapter;
import com.example.mangaxdroid.object.Chapter;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class ChapterMangaInfoFragment extends Fragment {
    ListView listView;
    ArrayList<Chapter> listChapter;
    Manga manga;
    CustomChapterListAdapter adapter;

    public ChapterMangaInfoFragment(Manga manga) {
        this.manga = manga;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        connectContent();

        listView = (ListView) view.findViewById(R.id.listManga);
        adapter = new CustomChapterListAdapter(view.getContext(), R.layout.chapter_list_custom_row, listChapter);
        listView.setAdapter(adapter);
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
        listChapter.add(new Chapter("1","1/1/1","0"));
    }
}
