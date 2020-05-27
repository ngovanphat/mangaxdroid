package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.TabAdapter;
import com.example.mangaxdroid.interfaces.FragmentCallBacks;
import com.example.mangaxdroid.object.Manga;
import com.google.android.material.tabs.TabLayout;

public class MangaInfoFragment extends Fragment implements FragmentCallBacks {
    private FragmentActivity myContext;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Manga manga;

    public static MangaInfoFragment newInstance(){
        MangaInfoFragment fragment = new MangaInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mangainfo, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabAdapter = new TabAdapter(myContext.getSupportFragmentManager());
        tabAdapter.addFragment(new InfoMangaInfoFragment(manga),"Thông tin");
        tabAdapter.addFragment(new CommentMangaInfoFragment(manga),"Bình luận");
        tabAdapter.addFragment(new ChapterMangaInfoFragment(manga),"Chapters");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        return view;
    }

    @Override
    public void onMsgFromMainToFragment(Manga manga) {
        this.manga = manga;
    }
}
