package com.example.mangaxdroid.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.LoginActivity;
import com.example.mangaxdroid.adapter.CommentAdapter;
import com.example.mangaxdroid.object.Comment;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

public class CommentMangaInfoFragment extends Fragment {
    ListView listView;
    Manga manga;
    EditText textComment;
    ImageView btnSend;
    ArrayList<Comment> listComments;
    CommentAdapter adapter;
    private DatabaseReference mDatabase;
// ...

    public CommentMangaInfoFragment(Manga manga) {
        this.manga = manga;
    }



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_comment_mangainfo, container, false);
        listComments = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.listComment);
        textComment = (EditText) view.findViewById(R.id.textComment);
        btnSend = (ImageView) view.findViewById(R.id.btnSend);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadContent(manga.getName());

        adapter = new CommentAdapter(view.getContext(), R.layout.comment_list_custom_row, listComments);
        listView.setAdapter(adapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                if (user==null) {
                    final Dialog notLoggedIn = new Dialog(view.getContext());
                    notLoggedIn.setContentView(R.layout.dialog_bookmark_sign_in);
                    TextView announce = (TextView) notLoggedIn.findViewById(R.id.announceTextView);
                    announce.setText("Vui lòng đăng nhập để sử dụng tính năng Comment");

                    Button login = (Button) notLoggedIn.findViewById(R.id.toLogIn);
                    login.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            startActivity(new Intent(view.getContext(), LoginActivity.class));
                            notLoggedIn.dismiss();
                        }
                    });
                    Button cancel= (Button) notLoggedIn.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            notLoggedIn.dismiss();
                        }
                    });
                    notLoggedIn.show();
                }
                else{
                    String message = textComment.getText().toString().trim();
                    if(!message.equals("")){
                    sendMessageToDatabase(message,user);
                    textComment.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    textComment.setText("");
                    }
                }
            }
        });
        return view;
    }
    private void sendMessageToDatabase(String message,FirebaseUser user){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.CHINA);
        Date time =  new Date();
        String username= "";
        if(!user.getDisplayName().equals("")){
            username = user.getDisplayName();
        }else{
            username = user.getEmail();
        }
        Comment comment = new Comment(message,dateFormat.format(time).toString(),username);
        mDatabase.child("Comments").child(manga.getId()).push().setValue(comment);
    }
    public void loadContent(final String nameManga){

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Comments/"+manga.getId());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listComments.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Comment comment = children.getValue(Comment.class);
                    listComments.add(comment);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
}
