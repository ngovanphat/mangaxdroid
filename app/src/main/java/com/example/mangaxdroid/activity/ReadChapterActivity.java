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
import com.example.mangaxdroid.fragment.ReadHorizontalFragment;
import com.example.mangaxdroid.fragment.ReadSettingsFragment;
import com.example.mangaxdroid.fragment.ReadVerticalFragment;
import com.example.mangaxdroid.object.Manga;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ReadChapterActivity extends AppCompatActivity implements ReadVerticalFragment.OnListviewListener, ReadHorizontalFragment.OnViewPagerListener, ReadSettingsFragment.OnReadSettingsListener {
    FragmentTransaction ft;
    ReadVerticalFragment readVertical;
    ReadHorizontalFragment readHorizontal;
    BottomNavigationView bottomNav;
    RelativeLayout layout;
    Toolbar toolbar;
    ActionBar actionBar;
    ArrayList<String> imgURLs=new ArrayList<String>();
    String chapterName;
    String mangaName;
    FrameLayout readerFrame;
    SharedPreferences sharedPreferences;
    ReadSettingsFragment settingsFragment;
    String viewType="Vertical";
    Button nextBtn;
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
        final Manga manga = (Manga) bundle.getSerializable("manga");
        mangaName = intent.getStringExtra("mangaName");
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
                switch (item.getItemId()) {
                    case R.id.action_setting:
                        Toast.makeText(getApplicationContext(),viewType,Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("currentViewType",viewType);
                        settingsFragment= ReadSettingsFragment.newInstance(bundle);
                        settingsFragment.show(getSupportFragmentManager(),"dialog");
                        break;
                    case R.id.action_chapterList:
                        Toast.makeText(ReadChapterActivity.this, "chapter list", Toast.LENGTH_SHORT).show();
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

}
