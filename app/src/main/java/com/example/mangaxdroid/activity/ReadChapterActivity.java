package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.fragment.ReadVerticalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class ReadChapterActivity extends AppCompatActivity implements ReadVerticalFragment.OnListviewListener{
    FragmentTransaction ft;
    ReadVerticalFragment readVertical;
    ChapterAdapter chapterAdapter;
    BottomNavigationView bottomNav;
    RelativeLayout layout;
    Toolbar toolbar;
    ActionBar actionBar;
    ArrayList<String> imgURLs=new ArrayList<String>();
    String chapterName;
    FrameLayout readerFrame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);
        readerFrame=findViewById(R.id.readerFrame);
        //lấy tên & số chap
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            chapterName = extras.getString("id")+":"+extras.getString("Name");
        }


        ft=getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("mangaID","auto_generated_id");
        bundle.putString("chapterID","auto_generated_id");
        readVertical= ReadVerticalFragment.newInstance(bundle);
        ft.replace(R.id.readerFrame,readVertical);
        ft.commit();

        bottomNav=findViewById(R.id.navBar);
        layout = findViewById(R.id.baseLayout);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chapter x: abc");
        bottomNav.setItemIconTintList(null);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_setting:
                        Toast.makeText(ReadChapterActivity.this, "setting", Toast.LENGTH_SHORT).show();
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
}
