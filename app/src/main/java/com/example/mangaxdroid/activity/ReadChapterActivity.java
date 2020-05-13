package com.example.mangaxdroid.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class ReadChapterActivity extends AppCompatActivity {
    ListView listView;
    ChapterAdapter chapterAdapter;
    BottomNavigationView bottomNav;
    RelativeLayout layout;
    Toolbar toolbar;
    ActionBar actionBar;
    List<String> imgURLs=new ArrayList<String>();;
    private DatabaseReference dbRef;
    int[] images= {R.drawable.pg1, R.drawable.pg2, R.drawable.pg3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_read_chapter);
        int fl=fetchChapter("abc","auto_generated_id");
        listView=findViewById(R.id.imgList);
        if(fl==0)
            listView.setAdapter(new ChapterAdapter(this,R.layout.chapter_item, imgURLs));

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
                    bottomNav.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }
                if (lastFirstVisibleItem > firstVisibleItem) {
                    bottomNav.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (bottomNav.getVisibility()==View.GONE) {
                    bottomNav.setVisibility(View.VISIBLE);
                    getSupportActionBar().show();
                }
                else {
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

    public int fetchChapter(String mangaId, final String chapterId){
        //TODO try running
        //dbRef= FirebaseDatabase.getInstance().getReference("temp/chapters/"+chapterId);
        //dbRef= FirebaseDatabase.getInstance().getReference("Mangas/"+mangaId+"/Chapters/"+chapterId);
        /*dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Chapter chapter = dataSnapshot.getValue(Chapter.class);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference("temp/chapters/" + chapterId);
                for (int i = 0; i < chapter.getPagesURL().size(); i++) {
                    String url=storageRef.child(chapter.getPagesURL().get(i)).getDownloadUrl().toString();
                    imgURLs.add(url);//URLs cho adapter truyền ảnh vào ImageViews
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });*/
        StorageReference listRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mangaxdroid.appspot.com/temp/chapters/auto_generated_id");
        /*listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            String url=item.getDownloadUrl().toString();
                            Toast.makeText(ReadChapterActivity.this, "setting", Toast.LENGTH_SHORT).show();
                            imgURLs.add(url);//URLs cho adapter truyền ảnh vào ImageViews
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });*/
        //TODO fix no auth token error
        imgURLs.add("https://firebasestorage.googleapis.com/v0/b/mangaxdroid.appspot.com/o/temp%2Fchapters%2Fauto_generated_id%2F1.jpg?alt=media");
        imgURLs.add("https://firebasestorage.googleapis.com/v0/b/mangaxdroid.appspot.com/o/temp%2Fchapters%2Fauto_generated_id%2F2.jpg?alt=media");
        imgURLs.add("https://firebasestorage.googleapis.com/v0/b/mangaxdroid.appspot.com/o/temp%2Fchapters%2Fauto_generated_id%2F3.jpg?alt=media");
        if (imgURLs==null)
            return 1;
        else return 0;
    }
    //Temp function for adding pictures with unique names
    public void uploadToServer(String mangaId,String chapterID){
        //TODO upload from res to create storage folders
        //upload drawables for now
        //set urls of images to each chapter
        //
    }
}
