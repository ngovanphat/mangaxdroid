package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.MangaAdapter;
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

public class OfflineFragment extends Fragment {
    ListView listView;
    ArrayList<Manga> historyMangas;
    MangaAdapter adapter;
    ArrayList<String> chapter;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_category, container, false);
        context = view.getContext();
        listView = (ListView) view.findViewById(R.id.listManga);
        historyMangas = new ArrayList<>();
        chapter = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReadChapterActivity.class);
                Bundle bundle = new Bundle();
                Manga manga = historyMangas.get(position);
                bundle.putSerializable("manga", manga);
                bundle.putString("numberChapter", chapter.get(position));
                intent.putExtras(bundle);
                listView.setAdapter(null);
                startActivity(intent);
            }
        });
        return view;
    }

    public void getHistoryList() {
        if (!historyMangas.isEmpty())
            historyMangas.clear();
        chapter = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final ArrayList<String> mangaListIds = new ArrayList<String>();
            final DatabaseReference hisdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("History");
            hisdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        mangaListIds.add(ds.getKey());
                        chapter.add((String) ds.child("Chapter").getValue());
                    }

//                    if (mangaListIds.isEmpty()) {
//                        AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
//                        myBuilder.setIcon(R.drawable.mangaxdroid)
//                                .setTitle("")
//                                .setMessage("\n\t\t\t\t\t\t\t\t\t\t\t\t\t\tDữ liệu trống.")
//                                .setPositiveButton("OK", null)
//                                .show();
//                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            hisdb.onDisconnect();
            final DatabaseReference mangadb = FirebaseDatabase.getInstance().getReference("Data/Mangas");
            mangadb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot cds : ds.getChildren()) {
                            if (mangaListIds.isEmpty()) {
                                adapter = new MangaAdapter(context, R.layout.manga_avatar, historyMangas);
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
            adapter = new MangaAdapter(context, R.layout.manga_avatar, historyMangas);
            listView.setAdapter(adapter);

//            AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
//            myBuilder.setIcon(R.drawable.mangaxdroid)
//                    .setTitle("\t\t\t\t\t\t\t\tThông báo")
//                    .setMessage("Bạn cần đăng nhập để xem lịch sử tải truyện.")
//                    .setPositiveButton("OK", null)
//                    .show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getHistoryList();
    }
}
