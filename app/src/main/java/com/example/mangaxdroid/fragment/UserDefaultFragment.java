package com.example.mangaxdroid.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.LoginActivity;
import com.example.mangaxdroid.activity.MainActivity;
import com.example.mangaxdroid.activity.useractivity.UserFaqActivity;
import com.example.mangaxdroid.activity.useractivity.UserFavoriteListActivity;

public class UserDefaultFragment extends Fragment {
    private FragmentActivity myContext;
    LinearLayout buttonLogin, faq;

    public static UserDefaultFragment newInstance(){
        UserDefaultFragment fragment = new UserDefaultFragment();
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
        final View view = inflater.inflate(R.layout.fragment_me, container, false);
        buttonLogin = (LinearLayout) view.findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) view.getContext();
                mainActivity.getBottomNavigationView().setSelectedItemId(R.id.page_1);
                startActivity(new Intent(view.getContext(), LoginActivity.class));

            }
        });

        faq = (LinearLayout) view.findViewById(R.id.faq);
        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), UserFaqActivity.class));
            }
        });
        return view;
    }
}

