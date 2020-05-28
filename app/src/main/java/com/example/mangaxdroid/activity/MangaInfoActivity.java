package com.example.mangaxdroid.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.useractivity.UserFavoriteListActivity;
import com.example.mangaxdroid.fragment.MangaInfoFragment;
import com.example.mangaxdroid.object.Manga;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MangaInfoActivity extends AppCompatActivity {
    ImageView image;
    TextView name;
    FrameLayout frame;
    Context context;
    BottomNavigationView navigationBarMangaInfo;
    Toolbar toolbar;
    Manga manga;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_info_new);
        context = this.getApplicationContext();
        connectContent();

        toolbar = findViewById(R.id.toolBarMangaInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        manga = (Manga) bundle.getSerializable("manga");

        name.setText(manga.getName());
        Glide.with(this)
                .load(manga.getImage())
                .fitCenter()
                .into(image);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MangaInfoFragment MIF = MangaInfoFragment.newInstance();
        transaction.replace(R.id.frameMangaInfo, MIF);
        transaction.commit();

        MIF.onMsgFromMainToFragment(manga);

        checkFavorite();

        navigationBarMangaInfo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        onFavoriteClick();
                        break;
                    case R.id.action_recents:
                        break;
                }
                return true;
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

    private void connectContent() {
        name = (TextView) findViewById(R.id.mangaTitleNew);
        image = (ImageView) findViewById(R.id.mangaThumbnailNew);
        frame = (FrameLayout) findViewById(R.id.frameMangaInfo);
        navigationBarMangaInfo = (BottomNavigationView) findViewById(R.id.navigationBarMangaInfo);
    }
    private void checkFavorite(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            final DatabaseReference favdb= FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Favorite");
            favdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Menu menu = navigationBarMangaInfo.getMenu();
                    if (snapshot.hasChild(manga.getId())) {
                        menu.findItem(R.id.action_favorites).setIcon(R.drawable.heart_solid);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            favdb.onDisconnect();
        }
    }
    private void onFavoriteClick(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            final Dialog notLoggedIn=new Dialog(this);
            notLoggedIn.setContentView(R.layout.dialog_bookmark_sign_in);
            Button login = (Button) notLoggedIn.findViewById(R.id.toLogIn);
            login.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(MangaInfoActivity.this, LoginActivity.class));
                    notLoggedIn.dismiss();
                }
            });
            Button cancel= (Button) notLoggedIn.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    notLoggedIn.dismiss();
                }
            });
            notLoggedIn.show();
        }else{
            final DatabaseReference favdb=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Favorite");
            favdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Menu menu = navigationBarMangaInfo.getMenu();
                    String mangaId=manga.getId();
                    //add new
                    if (!snapshot.hasChild(mangaId)) {
                        //Toast.makeText(ReadChapterActivity.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
                        //bookmarkdb.child(mangaName).setValue(chapterName);
                        favdb.child(mangaId).setValue(manga.getName());
                        menu.findItem(R.id.action_favorites).setIcon(R.drawable.heart_solid);
                    }else {
                        //remove
                        favdb.child(mangaId).removeValue();
                        menu.findItem(R.id.action_favorites).setIcon(R.drawable.heart_regular);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            favdb.onDisconnect();
        }
    }
}
