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
import androidx.fragment.app.Fragment;

import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.HistoryAdapter;
import com.example.mangaxdroid.object.Manga;
import com.example.mangaxdroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    ListView listView;
    ArrayList<Manga> historyMangas;
    HistoryAdapter adapter;
    ArrayList<String> chapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) view.findViewById(R.id.listManga);
        historyMangas = new ArrayList<>();
        chapter = new ArrayList<>();
        if (!historyMangas.isEmpty())
            historyMangas.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final ArrayList<String> mangaListIds = new ArrayList<String>();
            final DatabaseReference favdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("History");
            favdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        mangaListIds.add(ds.getKey());
                        chapter.add((String) ds.child("Chapter").getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            favdb.onDisconnect();
            final DatabaseReference mangadb = FirebaseDatabase.getInstance().getReference("Data/Mangas");
            mangadb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot cds : ds.getChildren()) {
                            if (mangaListIds.isEmpty()) {
                                adapter = new HistoryAdapter(view.getContext(), R.layout.manga_avatar_history, historyMangas, chapter);
                                listView.setAdapter(adapter);
                            }
                            if (mangaListIds.indexOf(cds.getKey()) != -1) {
                                mangaListIds.remove(cds.getKey());
                                Manga temp = cds.getValue(Manga.class);
                                temp.setId(cds.getKey());
                                historyMangas.add(temp);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            mangadb.onDisconnect();
        }
        else {
            Log.d("User","Chua Dang Nhap");
            adapter = new HistoryAdapter(view.getContext(), R.layout.manga_avatar, historyMangas, chapter);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReadChapterActivity.class);
                Bundle bundle = new Bundle();
                Manga manga = historyMangas.get(position);
                bundle.putSerializable("manga", manga);
                bundle.putString("numberChapter", chapter.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }
}
