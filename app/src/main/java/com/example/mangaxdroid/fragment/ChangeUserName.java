package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.mangaxdroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangeUserName extends DialogFragment {
    Context context = null;
    EditText ogUserName;
    FirebaseUser user;
    Button save;
    ChangeUserName thisFragment = this;

    public static ChangeUserName newInstance() {
        ChangeUserName fragment = new ChangeUserName();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_username, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();

        ogUserName = (EditText) view.findViewById(R.id.ogUserName);

        loadUser(user);
        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = ogUserName.getText().toString().trim();
                final DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                userdb.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, Object> result = new HashMap<>();
                        result.put("/UserName", (String) name);
                        userdb.updateChildren(result);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                getActivity().getSupportFragmentManager().beginTransaction().remove(thisFragment).commit();
            }
        });
        return view;
    }

    public void loadUser (FirebaseUser user){
        final DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
        userdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ogUserName.setText((String)dataSnapshot.child("UserName").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        userdb.onDisconnect();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.30);
        getDialog().getWindow().setLayout(width, height);
    }
}
