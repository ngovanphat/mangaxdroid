package com.example.mangaxdroid;



import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class TabAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentList =  new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();


    public TabAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }

    public CharSequence getPageTitle(int position){
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
