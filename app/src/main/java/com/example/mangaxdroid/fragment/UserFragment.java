package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.useractivity.UserFavoriteListActivity;
import com.example.mangaxdroid.activity.useractivity.UserHistoryListActivity;
import com.example.mangaxdroid.activity.useractivity.UserOfflineListActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class UserFragment extends Fragment {
    private FragmentActivity myContext;
    private TextView readOffline, accountUpgrade, readHistory, listFavorite, accountSetting,username;
    private FirebaseUser user;
    private Button btnLogout;
    private ImageView userAvatar;
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
    public void loadUser(FirebaseUser user){
        String providerID="";

        for (UserInfo profile : user.getProviderData()) {
            providerID = profile.getProviderId();
        }
        //Log.d("loadUser",providerID);
        if(providerID.equals("facebook.com")||providerID.equals("google.com"))
        {
            username.setText(user.getDisplayName());
            Picasso.get().load(user.getPhotoUrl()).into(userAvatar);
        }else{
            username.setText(user.getEmail());
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_info, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        //accountUpgrade = (TextView) view.findViewById(R.id.accountUpgrade);
        accountSetting = (TextView) view.findViewById(R.id.accountSetting);
        username = (TextView) view.findViewById(R.id.userEmail);
        userAvatar = (ImageView) view.findViewById(R.id.userAvatar);
        btnLogout = (Button) view.findViewById(R.id.buttonLogOut);
        if(user!=null)
        loadUser(user);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager loginManager = LoginManager.getInstance();
                if(loginManager!=null){
                    loginManager.logOut();
                }
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameMain, UserDefaultFragment.newInstance());
                transaction.commit();
            }
        });
        readOffline = (TextView) view.findViewById(R.id.readOffline);
        readOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), UserOfflineListActivity.class);
                startActivity(intent);
            }
        });
        listFavorite = (TextView) view.findViewById(R.id.listFavorite);
        listFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), UserFavoriteListActivity.class);
                startActivity(intent);
            }
        });
        readHistory = (TextView) view.findViewById(R.id.readHistory);
        readHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), UserHistoryListActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
