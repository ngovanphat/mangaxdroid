package com.example.mangaxdroid.activity.useractivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.fragment.ChangePassword;
import com.example.mangaxdroid.fragment.ChangeUserName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSettingActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button nameChange, passwordChange;
    TextView changeName, changePassword;
    ChangeUserName changeUserName;
    ChangePassword changePW;
    FirebaseUser user;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        user = FirebaseAuth.getInstance().getCurrentUser();

        changeName = (TextView) findViewById(R.id.changeLoginName);
        changePassword = (TextView) findViewById(R.id.changePassword);

        toolbar = (Toolbar) findViewById(R.id.toolBarFavorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadUser(user);
        nameChange = (Button) findViewById(R.id.nameChange);
        nameChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String providerID = "";
                for (UserInfo profile : user.getProviderData()) {
                    providerID = profile.getProviderId();
                }
                if (providerID.equals("facebook.com") || providerID.equals("google.com"))
                {
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(UserSettingActivity.this);
                    myBuilder.setIcon(R.drawable.mangaxdroid)
                            .setTitle("Thông báo")
                            .setMessage("Bạn không thể thay đổi tên người dùng khi đăng nhập bằng tài khoản Facebook hoặc Google !")
                            .setPositiveButton("OK", null)
                            .show();
//                    changeUserName = ChangeUserName.newInstance();
//                    changeUserName.show(getSupportFragmentManager(), "dialog");
                }
                else {
                    changeUserName = ChangeUserName.newInstance();
                    changeUserName.show(getSupportFragmentManager(), "dialog");

//                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(UserSettingActivity.this);
//                    myBuilder.setIcon(R.drawable.mangaxdroid)
//                            .setTitle("Thông báo")
//                            .setMessage("Bạn không thể thay đổi tên người dùng khi đăng nhập bằng tài khoản Facebook hoặc Google !")
//                            .setPositiveButton("OK", null)
//                            .show();
                }
            }
        });

        passwordChange = (Button) findViewById(R.id.passwordChange);
        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String providerID = "";
                for (UserInfo profile : user.getProviderData()) {
                    providerID = profile.getProviderId();
                }
                if (providerID.equals("facebook.com") || providerID.equals("google.com"))
                {
                    AlertDialog.Builder myBuilder = new AlertDialog.Builder(UserSettingActivity.this);
                    myBuilder.setIcon(R.drawable.mangaxdroid)
                            .setTitle("Thông báo")
                            .setMessage("Bạn không thể thay đổi tên mật khẩu khi đăng nhập bằng tài khoản Facebook hoặc Google !")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else {
                    changePW = ChangePassword.newInstance();
                    changePW.show(getSupportFragmentManager(), "dialog");
                }
            }
        });
    }

    public void loadUser (FirebaseUser user){
        String providerID = "";

        for (UserInfo profile : user.getProviderData()) {
            providerID = profile.getProviderId();
        }
        if (providerID.equals("facebook.com") || providerID.equals("google.com"))
        {
            changeName.setText(user.getDisplayName());
        }
        else {
            final DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            userdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    changeName.setText((String)dataSnapshot.child("UserName").getValue());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            userdb.onDisconnect();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
