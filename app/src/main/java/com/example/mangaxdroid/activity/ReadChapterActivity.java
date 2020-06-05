package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.fragment.ReadChapterListFragment;
import com.example.mangaxdroid.fragment.ReadHorizontalFragment;
import com.example.mangaxdroid.fragment.ReadSettingsFragment;
import com.example.mangaxdroid.fragment.ReadVerticalFragment;
import com.example.mangaxdroid.object.DownloadManga;
import com.example.mangaxdroid.object.Manga;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.SyncTree;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.concurrent.CountDownLatch;

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
    ArrayList<Integer> pagesLoaded= new ArrayList<Integer>();
    boolean isRead=false;
    long totalPages=Long.MAX_VALUE;//để khi chưa lấy được thông tin chapter thì cũng không + view lên
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
        bottomNav=findViewById(R.id.navBar);
        layout = findViewById(R.id.baseLayout);
        toolbar = findViewById(R.id.toolBar);
        //lấy tên & số chap
        LottieCompositionFactory.fromRawRes(this,R.raw.checkmark_animation);
        LottieCompositionFactory.fromRawRes(this,R.raw.error_animation);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        manga = (Manga) bundle.getSerializable("manga");
        mangaName = manga.getName().toUpperCase().toString();
        chapterName = intent.getStringExtra("numberChapter");
        CountDownLatch done = new CountDownLatch(1);
        ft=getSupportFragmentManager().beginTransaction();
        bundle = new Bundle();
        bundle.putSerializable("manga",manga);
        bundle.putString("chapterID",chapterName);
        readHorizontal=ReadHorizontalFragment.newInstance(bundle);
        if(viewType.equals("Vertical"))
        {
            readVertical= ReadVerticalFragment.newInstance(bundle);
            ft.replace(R.id.readerFrame,readVertical);
            ft.commit();
        }else if(viewType.equals("Horizontal")){
            readHorizontal=ReadHorizontalFragment.newInstance(bundle);
            ft.replace(R.id.readerFrame,readHorizontal);
            ft.commit();
        }

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
                        onDownloadClick(manga,chapterName);
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
    protected void onPause() {
        super.onPause();
        toHistory();
    }

    //----------------------------------
    //Listeners
    @Override
    public void getChapterSize(long size) {
        totalPages=size;
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
        if (readHorizontal.isResumed()&&setViewType.equals("Vertical")) {
            viewType="Vertical";
            toHistory();
            Bundle bundle = new Bundle();
            bundle.putSerializable("manga",manga);
            bundle.putString("chapterID",chapterName);
            bundle.putInt("pageCount",currentPage);
            readVertical= ReadVerticalFragment.newInstance(bundle);
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.readerFrame,readVertical);
            ft.commit();
        }else if(readVertical.isResumed()&&setViewType=="Horizontal")
        {
            viewType="Horizontal";
            toHistory();
            Bundle bundle = new Bundle();
            bundle.putSerializable("manga",manga);
            bundle.putString("chapterID",chapterName);
            bundle.putInt("pageCount",currentPage);
            readHorizontal=ReadHorizontalFragment.newInstance(bundle);
            ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.readerFrame,readHorizontal);
            ft.commit();
        }
    }

    @Override
    public void onCurrentPageUpdate(int curPage){
        if(!isRead &&(pagesLoaded.size()>totalPages*50/100)){
            addViewCount(mangaName,chapterName);
            isRead=true;
        }
        if(!pagesLoaded.contains(curPage)){
            pagesLoaded.add(curPage);
        }
        currentPage=curPage;
    }
    //----------------------------------
    //General
    private void resetVariables(){
        isRead=false;
        pagesLoaded=new ArrayList<Integer>();
        totalPages=Long.MAX_VALUE;
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
                    resetVariables();
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
    private void addViewCount(String mangaName, final String chapterId){
        dbRef= FirebaseDatabase.getInstance().getReference().child("Data").child("Chapters").child(mangaName.toUpperCase()).child(chapterId);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("view")){
                    int curViewCount=Integer.parseInt(dataSnapshot.child("view").getValue().toString());
                    dbRef.child("view").setValue(curViewCount+1);
                }
                else dbRef.child("view").setValue(1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dbRef.onDisconnect();
    }

    @Override
    public void onReportSubmit(final String topic,final String details){
        if(!isNetworkAvailable()){
            finishReportDialog(1);
            return;
        }
        final DatabaseReference reportDb= FirebaseDatabase.getInstance().getReference("Reports");
        final Query reportQuery=reportDb.orderByChild("updatedAt");
        reportQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String mangaId=manga.getId();
                String key =  reportDb.push().getKey();
                Date date = new Date();
                //This method returns the time in millis
                long timeMilli = date.getTime();
                //add new
                reportDb.child(key).child("Manga").setValue(mangaId);
                reportDb.child(key).child("Chapter").setValue(chapterName);
                reportDb.child(key).child("Topic").setValue(topic);
                reportDb.child(key).child("Details").setValue(details);
                reportDb.child(key).child("createdAt").setValue(timeMilli,new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            finishReportDialog(0);
                        }else{
                            finishReportDialog(1);
                        }
                    }
                });
                int count=(int)snapshot.getChildrenCount();
                int limit=10;
                if(count>limit){
                    int counter=0;
                    for(DataSnapshot ds:snapshot.child(mangaId).getChildren()){
                        if(counter==limit){
                            reportDb.child(ds.getKey()).removeValue();
                        }
                        counter++;
                    }
                }

                reportDb.onDisconnect();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finishReportDialog(1);
            }
        });
        reportDb.onDisconnect();
    }
    private void finishReportDialog(int fl){
        final Dialog success = new Dialog(this);
        success.setContentView(R.layout.report_btn_success);
        final LottieAnimationView successAnimation=success.findViewById(R.id.successAnimationView);
        final ProgressBar animationProgress=success.findViewById(R.id.progressBar2);
        final TextView successDialogTxtView=success.findViewById(R.id.successDialogTxtView);
        if(fl==1) {
            successAnimation.setAnimation(R.raw.error_animation);
            successDialogTxtView.setText("Report Failed!\nPlease try again.");
        }
        successAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                successDialogTxtView.setVisibility(View.VISIBLE);
                animationProgress.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        success.setCanceledOnTouchOutside(false);
        success.setCancelable(false);
        success.show();
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (success.isShowing()) {
                    success.dismiss();
                }
            }
        };
        success.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });
        handler.postDelayed(runnable, 4000);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    //----------------------------------
    //Download file
    // xin cấp quyền truy cập vào bộ nhớ
    private void getPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
                );
            }
        }
    }
    //Đường dẫn của hình ảnh tải về được lưu tại /storage/emulated/0/Data/Tên truyện viết hoa/Số chap truyện
    private void onDownloadClick(final Manga manga, final String chapterName){
        getPermission();
        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
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
        }
        else {
            DownloadManga downloadManga = new DownloadManga(manga, chapterName, ReadChapterActivity.this);
            final ArrayList<String> getURL = downloadManga.fetchChapter(mangaName, chapterName);
            final ArrayList<String> urlDownload = new ArrayList<>();
            final String path = "/Data/" + mangaName + "/" + chapterName + "/";
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    urlDownload.clear();
                    for (int i = 0; i < getURL.size(); i++) {
                        final int finalI = i;
                        Glide.with(ReadChapterActivity.this).load(getURL.get(i)).into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                super.onLoadFailed(errorDrawable);
                                File storage = new File(Environment.getExternalStorageDirectory() +path);
                                storage.delete();
                                Toast.makeText(ReadChapterActivity.this,"Tải về thất bại, vui lòng thử lại sau",Toast.LENGTH_LONG).show();
                                return;
                            }

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                String saveImageLink = saveImage(resource, "image" + finalI + ".jpeg", path);
                                urlDownload.add(saveImageLink);
                                if(finalI==getURL.size()-1){
                                    Toast.makeText(ReadChapterActivity.this,"Tải hoàn tất",Toast.LENGTH_LONG).show();
                                    writeToDatabaseMangaDownloaded(manga,user,chapterName,urlDownload);
                                }
                            }
                        });
                    }
                }
            }, 500);
        }
    }
    private void writeToDatabaseMangaDownloaded(Manga manga,FirebaseUser user,String chapterID,ArrayList<String> urlDownload){
        final DatabaseReference offlineDB=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("Offline");
        offlineDB.child(manga.getId()).child(chapterID).setValue(urlDownload);
    }
    private String saveImage(Drawable resource,String fileName,String path){
        String savedImagePath = null;
        //tạo thư mục lưu trữ ảnh truyện
        File storage = new File(Environment.getExternalStorageDirectory() +path);
        boolean success = true;
        if (!storage.exists()) {
           success = storage.mkdirs();
        }
       // Log.d("saveImage", String.valueOf(success)+ storage.getAbsolutePath());
        if (success) {
            File imageFile = new File(storage, fileName);
            savedImagePath = imageFile.getAbsolutePath();
         //   Log.d("path",savedImagePath);
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                //dùng class này để chuyển từ drawable qua bit map
                BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                bitmapDrawable.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        // Dòng phía dưới để thêm hình ảnh vào gallery hình ảnh của người dùng
          //  galleryAddPic(savedImagePath);
            //Toast.makeText(ReadChapterActivity.this, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }
    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

}
