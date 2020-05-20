package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.object.Manga;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReadHorizontalFragment extends Fragment {
    private static String chapterID;
    private DatabaseReference dbRef;
    private static Manga manga;
    private static String mangaID;
    ViewPager viewPager;
    ArrayList<String> imgURLs=new ArrayList<String>();;
    Context context=null;
    public static ReadHorizontalFragment newInstance(Bundle bundle) {
        ReadHorizontalFragment fragment=new ReadHorizontalFragment();
        manga = (Manga) bundle.getSerializable("manga");
        mangaID=manga.getName();
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
        FrameLayout layout=(FrameLayout) inflater.inflate(R.layout.fragment_read_horizontal, container, false);
        //lấy ảnh & đổ ảnh vào listView
        //chapter có id tự động, tìm bằng id lưu trong thông tin của mỗi chap
        viewPager=layout.findViewById(R.id.viewPager);
        imgURLs=fetchChapter(mangaID,chapterID);
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
                    ChapterPagerAdapter pagerAdapter=new ChapterPagerAdapter(getActivity(),imgURLs);
                    viewPager.setAdapter(pagerAdapter);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity().getApplicationContext(),"An error occured while loading pages",Toast.LENGTH_SHORT).show();
            }
        });
        return imgURLs;
    }
    public interface OnViewPagerListener{
        void onViewPagerClick();
    }
    public class ChapterPagerAdapter extends PagerAdapter {
        private Context context;
        ArrayList<String> imgURLs;
        ChapterPagerAdapter(Context context, ArrayList<String> imgURLs) {
            this.context = context;
            this.imgURLs = imgURLs;
        }
        @Override
        public int getCount () {
            return imgURLs.size();
        }

        @Override
        public boolean isViewFromObject (@NonNull View view, @NonNull Object object){
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem (@NonNull ViewGroup container,int position){
            LayoutInflater layoutinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View ve = layoutinflater.inflate(R.layout.chapter_item_resizable, null);

            final PhotoView photoView =new PhotoView(context);
            //Picasso.get().load(imgURLs.get(position)).fit().centerCrop().into(photoView);
            Picasso.get().load(imgURLs.get(position)).into(photoView);
            photoView.setMinimumScale(1);
            photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
                @Override
                public void onMatrixChanged(RectF rect) {
                    photoView.setAllowParentInterceptOnEdge(photoView.getScale() == 1);
                }
            });
            photoView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    ((OnViewPagerListener)context).onViewPagerClick();
                    return false;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    photoView.setScale(1);
                    return false;
                }
            });

            container.addView(photoView);

            return photoView;
        }

        @Override
        public void destroyItem (@NonNull ViewGroup container,int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
