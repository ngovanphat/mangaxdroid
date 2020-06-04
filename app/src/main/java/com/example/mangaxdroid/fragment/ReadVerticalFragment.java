package com.example.mangaxdroid.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.MangaInfoActivity;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Iterator;


public class ReadVerticalFragment extends Fragment {
    private static String chapterID;
    private static Manga manga;
    private DatabaseReference dbRef;
    private static String mangaID;
    private ListView listView;
    ArrayList<String> imgURLs=new ArrayList<String>();
    SharedPreferences pageCountSharedPref;
    private int pageCount=0;
    private static int startPageCount;
    private Context context=null;
    public static ReadVerticalFragment newInstance(Bundle bundle) {
        ReadVerticalFragment fragment=new ReadVerticalFragment();
        manga = (Manga) bundle.getSerializable("manga");
        mangaID=manga.getName().toUpperCase().toString();
        chapterID=bundle.getString("chapterID");
        return fragment;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout=(FrameLayout) inflater.inflate(R.layout.fragment_read_vertical, container, false);
        //Page storing for history
        /*pageCount=0;//default of static int is 0
        pageCountSharedPref = getContext().getSharedPreferences("readPages",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pageCountSharedPref.edit();
        edit.putString("pageCount", String.valueOf(pageCount));
        edit.apply();*/
        checkHistory();
        Log.e("page count", "onCreateView: "+pageCount );
        imgURLs=fetchChapter(mangaID,chapterID);
        listView=layout.findViewById(R.id.imgList);
        final Button btnNext = new Button(context);
        btnNext.setText("Next chapter");
        btnNext.setTextColor(ContextCompat.getColor(context, R.color.white));
        btnNext.getBackground().setAlpha(50);
        btnNext.setPadding(0, 0, 0, 0);
        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        btnNext.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, size.y/10));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextChapter();
            }
        });
        listView.addFooterView(btnNext);
        Log.e("page count", "onCreateView: "+String.valueOf(pageCount) );

        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lastFirstVisibleItem != firstVisibleItem) {
                    //((OnListviewListener) context).onListviewScroll(1);
                    int pos = 0;
                    if (listView.getChildCount() > 1 && listView.getChildAt(0).getTop() < 0) pos++;
                    View item = listView.getChildAt(pos);
                    if(item.getTag()!=null){//nút next chapter
                        if(item.getTag().equals("loaded"))
                            ((OnListviewListener) context).onCurrentPageUpdate(firstVisibleItem);
                    }
                    //checkPageCount(manga.getName(),chapterID);
                }
            }
        });
        GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                ((OnListviewListener) context).onListviewClick();
                return false;
            }
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                ((OnListviewListener) context).onListviewScroll(1);
                return false;
            }
        };
        final GestureDetector gd = new GestureDetector(context,listener);
        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        };
        listView.setOnTouchListener(l);
        return layout;
    }

    private void toNextChapter() {
        dbRef = FirebaseDatabase.getInstance().getReference("Data/Chapters/"+mangaID);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double dif=Double.MAX_VALUE;
                String nextChapter=chapterID;
                double cur=Double.parseDouble(chapterID);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    double tmpdif = Double.parseDouble(key) - cur;
                    if (tmpdif > 0 && dif > tmpdif) {
                        dif = tmpdif;
                        nextChapter = key;
                    }
                }
                if(!nextChapter.equals(chapterID)){
                    chapterID=nextChapter;
                    Toast.makeText(context,"Chapter: "+nextChapter,Toast.LENGTH_SHORT).show();
                    ((OnListviewListener) context).onChapterChange(nextChapter);
                    fetchChapter(mangaID,nextChapter);
                }else {
                    Toast.makeText(context,"Reached Last Chapter",Toast.LENGTH_SHORT).show();
                    String superClass = context.getClass().getSuperclass().getSimpleName();
                    if(superClass.equals(MangaInfoActivity.class.getName())){
                        ((OnListviewListener) context).onListviewClick();
                        ((OnListviewListener) context).onLastChapterClick();
                    }else{
                        Intent intent = new Intent(context, MangaInfoActivity.class);
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
    //TODO Loading effect
    //TODO Error shown by an image(or a button for retry image)
    public ArrayList<String> fetchChapter(String mangaName, final String chapterId){

        dbRef= FirebaseDatabase.getInstance().getReference().child("Data").child("Chapters").child(mangaName).child(chapterId).child("imageURL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> temp=new ArrayList<String>();
                ((OnListviewListener) context).getChapterSize(dataSnapshot.getChildrenCount());
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    temp.add(i,dataSnapshot.child(String.valueOf(i)).getValue().toString());//URLs cho adapter truyền ảnh vào ImageViews
                    imgURLs=temp;
                    listView.setAdapter(new ChapterAdapter(getActivity(),R.layout.chapter_item, imgURLs));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        dbRef.onDisconnect();//disconnect để sang activity khác
        return imgURLs;
    }

    private void checkHistory(){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            final DatabaseReference historyDb=FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("History");
            Query historyQuery=historyDb.orderByChild("updatedAt");
            historyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String mangaId=manga.getId();
                    if(snapshot.hasChild(mangaId)){
                        if(snapshot.child(mangaId).child("Chapter").getValue().toString().equals(chapterID)){
                            Log.e("check chapter get", "onDataChange: "+"got chapter " );
                            ((OnListviewListener) context).onCurrentPageUpdate(pageCount);
                            pageCount=Integer.parseInt(snapshot.child(mangaId).child("Page").getValue().toString());
                            listView.smoothScrollToPositionFromTop(pageCount,0);
                        }
                        else{
                            Log.e("check chapter get", "onDataChange: "+"did not get chapter" );
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
    public interface OnListviewListener{
        void getChapterSize(long size);
        void onListviewScroll(int flag);
        void onListviewClick();
        void onChapterChange(String nextChapter);
        void onLastChapterClick();
        void onCurrentPageUpdate(int curPage);
    }
}
