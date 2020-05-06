package com.example.mangaxdroid;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class ReadChapterActivity extends Activity {
    ListView listView;
    TextView textView;
    ChapterAdapter chapterAdapter;
    BottomNavigationView bottomNav;
    RelativeLayout layout;
    int[] images= {R.drawable.pg1, R.drawable.pg2, R.drawable.pg3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read_chapter);
        listView=findViewById(R.id.imgList);
        listView.setAdapter(new ChapterAdapter(this,R.layout.chapter_item,images));

        bottomNav=findViewById(R.id.navBar);
        layout = (RelativeLayout) findViewById(R.id.baseLayout);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNav.setVisibility(View.GONE);
            }
        });

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        Toast.makeText(ReadChapterActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites:
                        Toast.makeText(ReadChapterActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_nearby:
                        Toast.makeText(ReadChapterActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;                }
                return true;
            }
        });
    }

    public void fetchChapter(String id){
    //TODO fetch from server
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {//TODO finish hiding bottom navigation bar
        super.onTouchEvent(event);
        long startClickTime=0;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startClickTime = System.currentTimeMillis();
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (System.currentTimeMillis() - startClickTime < ViewConfiguration.getTapTimeout()) {
            }
            else {
                bottomNav.setVisibility(View.GONE);
            }
        }
        return true;
    }
}
