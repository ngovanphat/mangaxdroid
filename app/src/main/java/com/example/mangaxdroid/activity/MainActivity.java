package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.fragment.CategoriesFragment;
import com.example.mangaxdroid.fragment.HomeFragment;
import com.example.mangaxdroid.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity<DatabaseReference> extends FragmentActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectContent();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.page_1:
                                selectedFragment = HomeFragment.newInstance();
                                break;
                            case R.id.page_2:
                                selectedFragment = CategoriesFragment.newInstance();
                                break;
                            case R.id.page_5:
                                selectedFragment = UserFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.frameMain, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameMain, HomeFragment.newInstance());
        transaction.commit();
    }

    private void connectContent() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationBarMain);
    }
}
