package com.example.mangaxdroid;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class CategoriesFragment extends Fragment {
    private FragmentActivity myContext;
   private TabAdapter tabAdapter;
   private TabLayout tabLayout;
   private ViewPager viewPager;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        tabAdapter = new TabAdapter(myContext.getSupportFragmentManager());
        tabAdapter.addFragment(new HotCategoryFragment(),"Truyện Hot");
        tabAdapter.addFragment(new NewCategoryFragment(),"Truyện Mới");
        tabAdapter.addFragment(new ActionCategoryFragment(),"Hành Động");
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
