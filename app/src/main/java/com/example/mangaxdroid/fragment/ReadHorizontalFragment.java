package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.ChapterAdapter;
import com.example.mangaxdroid.object.Manga;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ReadHorizontalFragment extends Fragment {
    private static String chapterID;
    private DatabaseReference dbRef;
    private static Manga manga;
    private static String mangaID;
    ViewPager viewPager;
    private SharedPreferences pageCountSharedPref;
    ArrayList<String> imgURLs=new ArrayList<String>();;
    int pageCount=0;
    private static int startPageCount;
    Context context=null;
    public static ReadHorizontalFragment newInstance(Bundle bundle) {
        ReadHorizontalFragment fragment=new ReadHorizontalFragment();
        manga = (Manga) bundle.getSerializable("manga");
        mangaID=manga.getName().toUpperCase().toString();
        chapterID=bundle.getString("chapterID");
        startPageCount=bundle.getInt("pageCount");
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
        viewPager=layout.findViewById(R.id.viewPager);
        imgURLs=fetchChapter(mangaID,chapterID);
        if(startPageCount!=0)
            pageCount=startPageCount;
        checkHistory();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                View item=viewPager.getChildAt(position);
                if(item!=null&&item.getTag()!=null) {//nút next chapter
                    if (item.getTag().equals("loaded"))
                        ((OnViewPagerListener) context).onCurrentPageUpdate(position);
                }
            }
        });
        return layout;
    }
    //TODO Loading effect
    //TODO Error shown by an image(or a button for retry image)
    private ArrayList<String> fetchChapter(String mangaName, final String chapterId){
        dbRef= FirebaseDatabase.getInstance().getReference().child("Data").child("Chapters").child(mangaName).child(chapterId).child("imageURL");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> temp=new ArrayList<String>();
                ((OnViewPagerListener) context).getChapterSize(dataSnapshot.getChildrenCount());
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
                            ((ReadVerticalFragment.OnListviewListener) context).onCurrentPageUpdate(pageCount);
                            pageCount=Integer.parseInt(snapshot.child(mangaId).child("Page").getValue().toString());
                        }
                        else{
                            Log.e("check chapter get", "onDataChange: "+"did not get chapter" );
                        }
                    }
                    if(pageCount!=0)//để không ẩn menu khi vừa chuyển dạng xem (setCurrentItem trigger đổi trang)
                        viewPager.post(new Runnable() {
                            @Override
                            public void run() {
                                viewPager.setCurrentItem(pageCount);
                            }
                        });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            historyDb.onDisconnect();
        }
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
            final LayoutInflater layoutinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View ve = layoutinflater.inflate(R.layout.chapter_item_resizable, null);
            final ProgressBar progress=ve.findViewById(R.id.progress);
            final PhotoView photoView =new PhotoView(context);

            final View finalConvertView = ve;
            finalConvertView.setTag("loading");
            final TextDrawable errorDrawable =
                    TextDrawable.builder()
                            .beginConfig()
                            .textColor(Color.WHITE)
                            .fontSize(spToPx(14,context))
                            .height(Resources.getSystem().getDisplayMetrics().heightPixels/10)
                            .width(Resources.getSystem().getDisplayMetrics().widthPixels-dpToPx(20,context))
                            .bold()
                            .endConfig()
                            .buildRoundRect("Error while loading page",Color.parseColor("#870000"),dpToPx(15,context));
            //Picasso.get().load(imgURLs.get(position)).fit().centerCrop().into(photoView);
            //cannot use fit() here - if image not downloaded->fit won't finish-> loop no error
            //TODO fix progressBar not shown
            Picasso.get().load(imgURLs.get(position))
                    //.error(errorDrawable)
                    .into(photoView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progress.setVisibility(View.GONE);
                            photoView.setTag("loaded");
                            ve.setTag("loaded");
                        }
                        @Override
                        public void onError(Exception e) {
                            progress.setVisibility(View.GONE);
                            //photoView.setImageDrawable(errorDrawable);
                            photoView.setImageDrawable(errorDrawable);
                            ve.setTag("error");
                        }
                    });
            photoView.setMinimumScale(1);
            photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
                @Override
                public void onMatrixChanged(RectF rect) {
                    ((OnViewPagerListener)context).onViewPagerClick(1);
                    photoView.setAllowParentInterceptOnEdge(photoView.getScale() == 1);
                }
            });
            photoView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    ((OnViewPagerListener)context).onViewPagerClick(0);
                    return false;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    ((OnViewPagerListener)context).onViewPagerClick(1);
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
    static int spToPx(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
    static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public int convertToDp(int input) {
        final float scale =context.getResources().getDisplayMetrics().density;
        return (int) (input * scale + 0.5f);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public interface OnViewPagerListener{
        void getChapterSize(long size);
        void onViewPagerClick(int flag);
        void onCurrentPageUpdate(int curPage);
    }
}
