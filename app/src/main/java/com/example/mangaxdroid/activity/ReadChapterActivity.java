package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.fragment.ReadChapterListFragment;
import com.example.mangaxdroid.fragment.ReadHorizontalFragment;
import com.example.mangaxdroid.fragment.ReadSettingsFragment;
import com.example.mangaxdroid.fragment.ReadVerticalFragment;
import com.example.mangaxdroid.object.Manga;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadChapterActivity extends AppCompatActivity implements ReadVerticalFragment.OnListviewListener, ReadHorizontalFragment.OnViewPagerListener, ReadSettingsFragment.OnReadSettingsListener {
    //Controls
    ReadVerticalFragment readVertical;
    ReadHorizontalFragment readHorizontal;
    Manga manga;
    BottomNavigationView bottomNav;
    RelativeLayout layout;
    Toolbar toolbar;
    ActionBar actionBar;
    Button nextBtn;
    FrameLayout readerFrame;
    //Data
    FragmentTransaction ft;
    private DatabaseReference dbRef;
    ArrayList<String> imgURLs=new ArrayList<String>();
    String chapterName;
    String mangaName;
    //Menus & settings
    SharedPreferences sharedPreferences;
    ReadSettingsFragment settingsFragment;
    ReadChapterListFragment chapterListFragment;
    String viewType="Vertical";
    boolean flagShowNextBtn=true;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);
        readerFrame=findViewById(R.id.readerFrame);
        nextBtn=findViewById(R.id.toolbarbtn);
        //lấy tên & số chap
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        manga = (Manga) bundle.getSerializable("manga");
        mangaName = manga.getName();
        chapterName = intent.getStringExtra("numberChapter");

        ft=getSupportFragmentManager().beginTransaction();
        bundle = new Bundle();
        bundle.putSerializable("manga",manga);
        bundle.putString("chapterID",chapterName);
        readVertical= ReadVerticalFragment.newInstance(bundle);
        readHorizontal=ReadHorizontalFragment.newInstance(bundle);
        ft.replace(R.id.readerFrame,readVertical);
        ft.commit();
        /*final SharedPreferences settings=getSharedPreferences("settings",MODE_PRIVATE);
        SharedPreferences.Editor edit=settings.edit();
        viewType="Vertical";
        edit.putString("viewType",viewType);
        edit.apply();*/


        bottomNav=findViewById(R.id.navBar);
        layout = findViewById(R.id.baseLayout);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chapter " + chapterName);
        bottomNav.setItemIconTintList(null);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Bundle bundle = new Bundle();
                switch (item.getItemId()) {
                    case R.id.action_setting:
                        bundle.putString("currentViewType",viewType);
                        settingsFragment= ReadSettingsFragment.newInstance(bundle);
                        settingsFragment.show(getSupportFragmentManager(),"dialog");
                        break;
                    case R.id.action_chapterList:
                        Toast.makeText(ReadChapterActivity.this, "chapter list", Toast.LENGTH_SHORT).show();
                        bundle.putSerializable("manga",manga);
                        bundle.putString("chapterID",chapterName);
                        chapterListFragment=ReadChapterListFragment.newInstance(bundle);
                        chapterListFragment.show(getSupportFragmentManager(),"dialog");
                        break;
                    case R.id.action_bookmark:
                        Toast.makeText(ReadChapterActivity.this, "bookmark", Toast.LENGTH_SHORT).show();
                        break;                }
                return true;
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReadChapterActivity.this, "Next Chapter", Toast.LENGTH_SHORT).show();
                nextBtn.setEnabled(false);//Disable button from spamming
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // This method will be executed once the timer is over
                        nextBtn.setEnabled(true);
                    }
                },1000);
                toNextChapter();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListviewScroll(int flag) {
        if (flag==1) {
            bottomNav.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }
        if (flag==2) {
            bottomNav.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onListviewClick() {
        if(bottomNav.getVisibility()==View.GONE)
        {
            bottomNav.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        }else {
            bottomNav.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onChapterChange(String nextChapter) {
        getSupportActionBar().setTitle("Chapter " + nextChapter);
    }

    @Override
    public void onLastChapterClick() {
        finish();
    }

    @Override
    public void onViewPagerClick() {
        if(bottomNav.getVisibility()==View.GONE)
        {
            bottomNav.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        }else {
            bottomNav.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }
    }

    @Override
    public void OnReadSettingsChanged(String setViewType) {
        if (readHorizontal.isResumed()&&setViewType=="Vertical") {
            viewType="Vertical";
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.readerFrame,readVertical);
            ft.commit();
        }else if(readVertical.isResumed()&&setViewType=="Horizontal")
        {
            viewType="Horizontal";
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.readerFrame,readHorizontal);
            ft.commit();
        }
    }
    private void toNextChapter(){
        dbRef = FirebaseDatabase.getInstance().getReference("Data/Chapters/"+mangaName);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double dif=Double.MAX_VALUE;
                String nextChapter=chapterName;
                double cur=Double.parseDouble(chapterName);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    double tmpdif = Double.parseDouble(key) - cur;
                    if (tmpdif > 0 && dif > tmpdif) {
                        dif = tmpdif;
                        nextChapter = key;
                    }
                }
                Toast.makeText(ReadChapterActivity.this,"Chapter: "+nextChapter,Toast.LENGTH_SHORT).show();
                if(!nextChapter.equals(chapterName)){//To next chapter
                    chapterName=nextChapter;
                    getSupportActionBar().setTitle("Chapter " + chapterName);
                    ft=getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("manga",manga);
                    bundle.putString("chapterID",chapterName);
                    if(viewType.equals("Vertical"))
                    {
                        readVertical= ReadVerticalFragment.newInstance(bundle);
                        ft.replace(R.id.readerFrame,readVertical);
                    }
                    else{
                        readHorizontal=ReadHorizontalFragment.newInstance(bundle);
                        ft.replace(R.id.readerFrame,readHorizontal);
                    }
                    ft.commit();
                }else {
                    Toast.makeText(ReadChapterActivity.this,"Reached Last Chapter",Toast.LENGTH_SHORT).show();
                    String superClass = ReadChapterActivity.this.getClass().getSuperclass().getSimpleName();
                    if(superClass.equals(MangaInfoActivity.class.getSimpleName())){
                        onLastChapterClick();
                    }else{
                        Intent intent = new Intent(ReadChapterActivity.this, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga",manga);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        dbRef.onDisconnect();
    }
}
