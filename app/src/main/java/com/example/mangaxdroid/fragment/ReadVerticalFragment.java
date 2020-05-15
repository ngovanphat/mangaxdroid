package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class ReadVerticalFragment extends Fragment {
    private static String chapterID;
    private DatabaseReference dbRef;
    private static String mangaID;
    ListView listView;

    ArrayList<String> imgURLs=new ArrayList<String>();
    Context context=null;
    public static ReadVerticalFragment newInstance(Bundle bundle) {
        ReadVerticalFragment fragment=new ReadVerticalFragment();
        mangaID=bundle.getString("mangaID");//truyen ten manga với chapter id ở đây
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

        imgURLs=fetchChapter("KHI TRÒ CHƠI ÁC MA BẮT ĐẦU","112");
        listView=layout.findViewById(R.id.imgList);

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

    //TODO Loading effect
    //TODO Error shown by an image(or a button for retry image)
    public ArrayList<String> fetchChapter(String mangaName, final String chapterId){

        dbRef= FirebaseDatabase.getInstance().getReference().child("Data").child("Chapters").child(mangaName).child(chapterId).child("imageURL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> temp=new ArrayList<String>();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    temp.add(i,dataSnapshot.child(String.valueOf(i)).getValue().toString());//URLs cho adapter truyền ảnh vào ImageViews
                    imgURLs=temp;
                    listView.setAdapter(new ChapterAdapter(getActivity(),R.layout.chapter_item, imgURLs));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        return imgURLs;
    }
    public interface OnListviewListener{
        void onListviewScroll(int flag);
        void onListviewClick();
    }
}
