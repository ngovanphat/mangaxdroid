package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.useractivity.UserFavoriteListActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {
    private FragmentActivity myContext;
    private TextView accountInfo, accountUpgrade, readHistory, listFavorite, accountSetting,username;
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
        if(user.getDisplayName()!=null){
            username.setText(user.getDisplayName());
        }else{
            username.setText(user.getEmail());
        }
        if(user.getPhotoUrl()!=null){
            Picasso.get().load(user.getPhotoUrl()).into(userAvatar);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_info, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        accountInfo = (TextView) view.findViewById(R.id.accountInfo);
        accountUpgrade = (TextView) view.findViewById(R.id.accountUpgrade);
        accountSetting = (TextView) view.findViewById(R.id.accountSetting);
        readHistory = (TextView) view.findViewById(R.id.readHistory);
        username = (TextView) view.findViewById(R.id.userEmail);
        userAvatar = (ImageView) view.findViewById(R.id.userAvatar);
        btnLogout = (Button) view.findViewById(R.id.buttonLogOut);
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
