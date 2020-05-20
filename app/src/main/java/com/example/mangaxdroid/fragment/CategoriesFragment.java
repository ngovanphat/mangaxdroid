package com.example.mangaxdroid.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.mangaxdroid.fragment.categorytab.ActionCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.AdventureCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.ComedyCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.HorrorCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.HotCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.LoveCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.MythCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.NewCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.ScifiCategoryFragment;
import com.example.mangaxdroid.fragment.categorytab.TimeTravelCategoryFragment;
import com.example.mangaxdroid.object.Manga;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class CategoriesFragment extends Fragment {
    private FragmentActivity myContext;
    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ListView listView;
    private MangaAdapter mangaAdapter;
    private ArrayList<Manga> mangaArrayList;
    private int SelectedPage = 0;
    public static CategoriesFragment newInstance(int number){
        CategoriesFragment fragment = new CategoriesFragment(number);
        return fragment;
    }

    @SuppressLint("ValidFragment")
    public CategoriesFragment(int selectedPage) {
        SelectedPage = selectedPage;
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
        viewPager.setCurrentItem(SelectedPage);
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
