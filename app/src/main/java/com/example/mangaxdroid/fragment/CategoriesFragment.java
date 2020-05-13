package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mangaxdroid.object.Manga;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    private FragmentActivity myContext;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView listView;
    private MangaAdapter mangaAdapter;
    ArrayList<Manga> mangaArrayList;
    public static CategoriesFragment newInstance(){
        CategoriesFragment fragment = new CategoriesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabAdapter = new TabAdapter(myContext.getSupportFragmentManager());
        tabAdapter.addFragment(new HotCategoryFragment(),"Truyện Hot");
        tabAdapter.addFragment(new NewCategoryFragment(),"Truyện Mới");
        tabAdapter.addFragment(new ActionCategoryFragment(),"Hành Động");
        tabAdapter.addFragment(new MythCategoryFragment(),"Huyền Huyễn");
        tabAdapter.addFragment(new HorrorCategoryFragment(),"Kinh Dị");
        tabAdapter.addFragment(new LoveCategoryFragment(),"Tình yêu");
        tabAdapter.addFragment(new AdventureCategoryFragment(),"Phiêu lưu");
        tabAdapter.addFragment(new ComedyCategoryFragment(),"Hài Hước");
        tabAdapter.addFragment(new ScifiCategoryFragment(),"Viễn Tưởng");
        tabAdapter.addFragment(new TimeTravelCategoryFragment(),"Xuyên Không");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //loadPage(0,myContext);

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
}
