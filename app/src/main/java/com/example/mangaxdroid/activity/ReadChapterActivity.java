package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ReadChapterActivity extends AppCompatActivity implements ReadVerticalFragment.OnListviewListener, ReadHorizontalFragment.OnViewPagerListener, ReadSettingsFragment.OnReadSettingsListener,ReadChapterListFragment.OnReadChapterListListener {
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
    int currentPage=0;
    SharedPreferences pageCountSharedPref;
    //Menus & settings
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
        mangaName = manga.getName().toUpperCase().toString();
        chapterName = intent.getStringExtra("numberChapter");

        ft=getSupportFragmentManager().beginTransaction();
        bundle = new Bundle();
        bundle.putSerializable("manga",manga);
        bundle.putString("chapterID",chapterName);
        readVertical= ReadVerticalFragment.newInstance(bundle);
        readHorizontal=ReadHorizontalFragment.newInstance(bundle);
        ft.replace(R.id.readerFrame,readVertical);
        ft.commit();
        pageCountSharedPref = getSharedPreferences("readPages",MODE_PRIVATE);
        SharedPreferences.Editor edit = pageCountSharedPref.edit();
        edit.putString("pageCount", "0");
        edit.apply();

        bottomNav=findViewById(R.id.navBar);
        layout = findViewById(R.id.baseLayout);
        toolbar = findViewById(R.id.toolBar);

        //checkDownloaded
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Chapter " + chapterName);
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
                        bundle.putSerializable("manga",manga);
                        bundle.putString("chapterID",chapterName);
                        chapterListFragment=ReadChapterListFragment.newInstance(bundle);
                        chapterListFragment.show(getSupportFragmentManager(),"dialog");
                        break;
                    case R.id.action_download:
                        //onDownloadClick
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
    public void onViewPagerClick(int flag) {
        if(flag==1){
            bottomNav.setVisibility(View.GONE);
            getSupportActionBar().hide();
        }else{
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

    @Override
    public void OnReadSettingsChanged(String setViewType) {
        if (readHorizontal.isResumed()&&setViewType=="Vertical") {
            viewType="Vertical";
            Log.e("current page", "OnReadSettingsChanged: "+currentPage );
            pageCountSharedPref = getSharedPreferences("readPages",MODE_PRIVATE);
            SharedPreferences.Editor edit = pageCountSharedPref.edit();
            edit.putString("pageCount", String.valueOf(currentPage));
            edit.apply();
            Bundle bundle = new Bundle();
            bundle.putSerializable("manga",manga);
            bundle.putString("chapterID",chapterName);
            readVertical= ReadVerticalFragment.newInstance(bundle);
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.readerFrame,readVertical);
            ft.commit();
        }else if(readVertical.isResumed()&&setViewType=="Horizontal")
        {
            viewType="Horizontal";
            Log.e("current page", "OnReadSettingsChanged: "+currentPage );
            pageCountSharedPref = getSharedPreferences("readPages",MODE_PRIVATE);
            SharedPreferences.Editor edit = pageCountSharedPref.edit();
            edit.putString("pageCount", String.valueOf(currentPage));
            edit.apply();

            Bundle bundle = new Bundle();
            bundle.putSerializable("manga",manga);
            bundle.putString("chapterID",chapterName);
            readHorizontal=ReadHorizontalFragment.newInstance(bundle);
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
    /*private void checkBookmark(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            final DatabaseReference bookmarkdb=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Bookmark");
            bookmarkdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.hasChild(mangaName)) {
                        String check = snapshot.child(mangaName).getValue().toString();
                        if (check.equals(chapterName)) {
                            //menu mỗi lần xài phải gọi riêng, ko là báo lỗi
                            Menu menu = bottomNav.getMenu();
                            menu.findItem(R.id.action_bookmark).setIcon(R.drawable.bookmark_solid);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            bookmarkdb.onDisconnect();
        }
    }
    private void onBookMarkClick(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            final Dialog notLoggedIn=new Dialog(this);
            notLoggedIn.setContentView(R.layout.dialog_bookmark_sign_in);
            Button login = (Button) notLoggedIn.findViewById(R.id.toLogIn);
            login.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(ReadChapterActivity.this, LoginActivity.class));
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
            final DatabaseReference bookmarkdb=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Bookmark");
            bookmarkdb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Menu menu = bottomNav.getMenu();
                    //add new
                    if (!snapshot.hasChild(mangaName)) {
                        Toast.makeText(ReadChapterActivity.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
                        bookmarkdb.child(mangaName).setValue(chapterName);
                        menu.findItem(R.id.action_bookmark).setIcon(R.drawable.bookmark_solid);
                    }else {
                        String check=snapshot.child(mangaName).getValue().toString();
                        if (check.equals(chapterName)) {//remove
                            Toast.makeText(ReadChapterActivity.this, "Bookmark Removed", Toast.LENGTH_SHORT).show();
                            bookmarkdb.child(mangaName).removeValue();
                            menu.findItem(R.id.action_bookmark).setIcon(R.drawable.bookmark_regular);
                        }else {//change
                            Toast.makeText(ReadChapterActivity.this, "Bookmark Updated", Toast.LENGTH_SHORT).show();
                            bookmarkdb.child(mangaName).setValue(chapterName);
                            menu.findItem(R.id.action_bookmark).setIcon(R.drawable.bookmark_solid);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            bookmarkdb.onDisconnect();
        }
    }*/
    private void toHistory(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            final DatabaseReference historyDb=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("History");
            Query historyQuery=historyDb.orderByChild("updatedAt");
            historyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String mangaId=manga.getId();
                    Date date = new Date();
                    //This method returns the time in millis
                    long timeMilli = date.getTime();
                    //add new
                    historyDb.child(mangaId).child("Chapter").setValue(chapterName);
                    historyDb.child(mangaId).child("Page").setValue(currentPage);//get current page count
                    historyDb.child(mangaId).child("updatedAt").setValue(timeMilli);
                    int count=(int)snapshot.getChildrenCount();
                    if(count>10){
                        int counter=0;
                        for(DataSnapshot ds:snapshot.child(mangaId).getChildren()){
                            if(counter==10){
                                //node thứ 11
                                historyDb.child(ds.getKey()).removeValue();
                            }
                            counter++;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            historyDb.onDisconnect();
        }
    }

    @Override
    protected void onPause() {
        toHistory();
        super.onPause();
    }

    @Override
    public void OnChapterListItemClick(String chapterID) {
        getSupportActionBar().setTitle("Chapter " + chapterID);
        ft=getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("manga",manga);
        bundle.putString("chapterID",chapterID);
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
    }
    @Override
    public void onCurrentPageUpdate(int curPage){
        Log.d("read page",String.valueOf(curPage));
        currentPage=curPage;
    }
}
