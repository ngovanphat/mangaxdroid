package com.example.mangaxdroid.fragment;

import android.app.Activity;
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
import com.example.mangaxdroid.activity.MainActivity;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;


public class ReadVerticalFragment extends Fragment {
    private static String mangaID;
    ListView listView;
    List<String> imgURLs=new ArrayList<String>();;
    Activity activity;
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
        int fl=fetchChapter("abc","auto_generated_id");
        listView=layout.findViewById(R.id.imgList);
        if(fl==0)
            listView.setAdapter(new ChapterAdapter(getActivity(),R.layout.chapter_item, imgURLs));
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
        if(imgURLs==null)
            return 1;
        else return 0;
    }
    public interface OnListviewListener{
        void onListviewScroll(int flag);
        void onListviewClick();
    }
}
