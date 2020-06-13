package com.example.mangaxdroid.activity.useractivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class UserOfflineListActivity extends AppCompatActivity {
    private ArrayList<Manga> offlineMangas = new ArrayList<>();
    MangaAdapter adapter;
    ListView listView;
    Toolbar toolbar;
    ArrayList<String> chapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline);

        listView = (ListView) findViewById(R.id.listFavorites);
        toolbar = (Toolbar) findViewById(R.id.toolBarFavorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(view.getContext(), ReadChapterActivity.class);
//                Bundle bundle= new Bundle();
//                Manga manga = offlineMangas.get(position);
//                bundle.putSerializable("manga",manga);
//                bundle.putString("numberChapter", chapter.get(position));
//                intent.putExtras(bundle);
//                listView.setAdapter(null);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getHistoryList(){
        if (!offlineMangas.isEmpty())
            offlineMangas.clear();
        chapter = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final ArrayList<String> mangaListIds = new ArrayList<String>();
            final DatabaseReference offdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("History");
            offdb.addValueEventListener(new ValueEventListener() {
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
            offdb.onDisconnect();
            final DatabaseReference mangadb = FirebaseDatabase.getInstance().getReference("Data/Mangas");
            mangadb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        for (DataSnapshot cds : ds.getChildren()) {
                            if (mangaListIds.isEmpty()) {
                                adapter = new MangaAdapter(UserOfflineListActivity.this, R.layout.manga_avatar, offlineMangas);
                                listView.setAdapter(adapter);
                            }
                            if (mangaListIds.indexOf(cds.getKey()) != -1) {
                                mangaListIds.remove(cds.getKey());
                                Manga temp = cds.getValue(Manga.class);
                                temp.setId(cds.getKey());
                                offlineMangas.add(temp);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHistoryList();
    }
}
