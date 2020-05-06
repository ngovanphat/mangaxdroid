package com.example.mangaxdroid;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.List;

public class ReadChapterActivity extends AppCompatActivity {
    ListView listView;
    ChapterAdapter chapterAdapter;
    BottomNavigationView bottomNav;
    RelativeLayout layout;
    Toolbar toolbar;
    ActionBar actionBar;
    int[] images= {R.drawable.pg1, R.drawable.pg2, R.drawable.pg3};
    List<String> imgURLs;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_chapter);
        listView=findViewById(R.id.imgList);
        listView.setAdapter(new ChapterAdapter(this, R.layout.chapter_item, images));
        bottomNav=findViewById(R.id.navBar);
        layout = (RelativeLayout) findViewById(R.id.baseLayout);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chapter x: abc");

        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lastFirstVisibleItem < firstVisibleItem) {
                    Toast.makeText(getApplicationContext(), "Scrolling down the listView",
                            Toast.LENGTH_SHORT).show();
                    bottomNav.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }
                if (lastFirstVisibleItem > firstVisibleItem) {
                    Toast.makeText(getApplicationContext(), "Scrolling up the listView",
                            Toast.LENGTH_SHORT).show();
                    bottomNav.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(bottomNav.getVisibility()==View.GONE)
                {
                    bottomNav.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                }else {
                    bottomNav.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }
            }
        });




        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_setting:
                        //TODO show setting menu
                        Toast.makeText(ReadChapterActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_chapterList:
                        //TODO show chapter list only(no manga info)
                        Toast.makeText(ReadChapterActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_bookmark:
                        //TODO bookmark then show toast of success
                        Toast.makeText(ReadChapterActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
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
    public void fetchChapter(String mangaId, final String chapterId){
    //TODO try running
        dbRef= FirebaseDatabase.getInstance().getReference("Mangas/"+mangaId+"/Chapters/"+chapterId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chapter chapter = dataSnapshot.getValue(Chapter.class);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("chapters/" + chapterId);
                for (int i = 0; i < chapter.getPagesURL().size(); i++) {
                    String url=storageRef.child(chapter.getPagesURL().get(i)).getDownloadUrl().toString();
                    imgURLs.add(url);//URLs cho adapter truyền ảnh vào ImageViews
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    //Temp function for adding pictures with unique names
    public void uploadToServer(String mangaId,String chapterID){
        //TODO upload from res to create storage folders
        //upload drawables for now
        //set urls of images to each chapter
        //
    }
}
