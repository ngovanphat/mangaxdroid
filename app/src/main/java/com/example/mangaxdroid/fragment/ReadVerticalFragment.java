package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.object.Chapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;


public class ReadVerticalFragment extends Fragment {
    private DatabaseReference dbRef;
    private static String mangaID;
    ListView listView;
    List<String> imgURLs=new ArrayList<String>();;
    Activity activity;
    ChapterAdapter adapter;
    Context context=null;
    private static String chapterID="";
    public static ReadVerticalFragment newInstance(Bundle bundle) {
        ReadVerticalFragment fragment=new ReadVerticalFragment();
        mangaID=bundle.getString("mangaID");
        chapterID=bundle.getString("chapterID");
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FrameLayout layout=(FrameLayout) inflater.inflate(R.layout.fragment_read_vertical, container, false);
        //lấy ảnh & đổ ảnh vào listView
        //chapter có id tự động, tìm bằng id lưu trong thông tin của mỗi chap

        listView=layout.findViewById(R.id.imgList);
        adapter= new ChapterAdapter(getActivity(),R.layout.chapter_item, imgURLs);
        listView.setAdapter(adapter);
        int fl=fetchChapter("KHI TRÒ CHƠI ÁC MA BẮT ĐẦU","116");
        listView.setOnScrollListener(new AbsListView.OnScrollListener(){
            private int lastFirstVisibleItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lastFirstVisibleItem < firstVisibleItem) {
                    ((OnListviewListener) context).onListviewScroll(1);
                }
                if (lastFirstVisibleItem > firstVisibleItem) {
                    ((OnListviewListener) context).onListviewScroll(1);
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ((OnListviewListener) context).onListviewClick();
            }
        });
        return layout;
    }
    public int fetchChapter(String mangaName, final String chapterId){
        Log.d("Fetch chapter","fetching...");
        dbRef= FirebaseDatabase.getInstance().getReference().child("Data").child("Chapters").child(mangaName).child(chapterId).child("imageURL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Fetch chapter",dataSnapshot.getChildrenCount()+" pages found.");
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    Log.d("Fetch chapter","fetching page "+i);
                    imgURLs.add(dataSnapshot.child(String.valueOf(i)).getValue().toString());//URLs cho adapter truyền ảnh vào ImageViews
                    Log.d("Fetch chapter","imglen: "+imgURLs.get(i));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        if(imgURLs==null)
            return 1;
        else return 0;
    }
    public interface OnListviewListener{
        void onListviewScroll(int flag);
        void onListviewClick();
    }
}
