package com.example.mangaxdroid.activity.useractivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.MangaInfoActivity;
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

public class UserFavoriteListActivity extends AppCompatActivity {
    private ArrayList<Manga> favoriteMangas = new ArrayList<>();
    MangaAdapter adapter;
    ListView listView;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);

        listView = (ListView) findViewById(R.id.listFavorites);
        toolbar = (Toolbar) findViewById(R.id.toolBarFavorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getFavoriteList();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MangaInfoActivity.class);
                Bundle bundle= new Bundle();
                Manga manga = favoriteMangas.get(position);
                bundle.putSerializable("manga",manga);
                intent.putExtras(bundle);
                listView.setAdapter(null);//lúc back lại list thì load lại
                startActivity(intent);
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

    public void getFavoriteList(){
        if(!favoriteMangas.isEmpty())// clear để update nếu người dùng back trở lại
            favoriteMangas.clear();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){//check cho chắc thôi chứ mở được fragment này là phải log in rồi
            final ArrayList<String> mangaListIds=new ArrayList<String>();
            final DatabaseReference favdb= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Favorite");
            favdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()) {
                        mangaListIds.add(ds.getKey());
                    }
                    Log.d("List ids",mangaListIds.toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            favdb.onDisconnect();
            final DatabaseReference mangadb= FirebaseDatabase.getInstance().getReference("Data/Mangas");
            mangadb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren()) {//Từng thể loại
                        for(DataSnapshot cds : ds.getChildren()){//từng truyện trong thể loại
                            if(mangaListIds.isEmpty()){
                                adapter = new MangaAdapter(UserFavoriteListActivity.this, R.layout.manga_avatar, favoriteMangas);
                                listView.setAdapter(adapter);
                            }
                            Log.d("manga id ",String.valueOf(mangaListIds.indexOf(cds.getKey())));
                            if(mangaListIds.indexOf(cds.getKey())!=-1){//tìm thấy id
                                mangaListIds.remove(cds.getKey());//manga có thể ở nhiều thể loại => bỏ sau khi tìm thấy
                                Log.d("List ids",mangaListIds.toString());
                                Manga temp=cds.getValue(Manga.class);
                                temp.setId(cds.getKey());
                                favoriteMangas.add(temp);
                                Log.d("manga id ",favoriteMangas.toString());
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
    protected void onResume() {//gắn đây vì onResume có chạy lúc khởi động activity
        super.onResume();
        getFavoriteList();
    }
}
