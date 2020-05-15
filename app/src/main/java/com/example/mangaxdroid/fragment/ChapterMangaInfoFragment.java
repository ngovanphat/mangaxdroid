package com.example.mangaxdroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
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
        listChapter= new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listManga);
        adapter = new CustomChapterListAdapter(view.getContext(), R.layout.chapter_list_custom_row, listChapter);
        loadContent(manga.getName());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReadChapterActivity.class);
                intent.putExtra("mangaName",manga.getName());
                intent.putExtra("numberChapter",listChapter.get(position).getName());
                startActivity(intent);
            }
        });
        return view;
    }

    public void loadContent(String nameManga){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Chapters/"+nameManga);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listChapter.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    listChapter.add(new Chapter("Chapter " + data.getRef().getKey(),"15/05/2020","909"));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


}
