package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.useractivity.UserFavoriteListActivity;

public class UserFragment extends Fragment {
    private FragmentActivity myContext;
    private TextView accountInfo, accountUpgrade, readHistory, listFavorite, accountSetting;
    public static UserFragment newInstance(){
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
    }

    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_info, container, false);
        accountInfo = (TextView) view.findViewById(R.id.accountInfo);
        accountUpgrade = (TextView) view.findViewById(R.id.accountUpgrade);
        accountSetting = (TextView) view.findViewById(R.id.accountSetting);
        readHistory = (TextView) view.findViewById(R.id.readHistory);
        listFavorite = (TextView) view.findViewById(R.id.listFavorite);

        listFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), UserFavoriteListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
