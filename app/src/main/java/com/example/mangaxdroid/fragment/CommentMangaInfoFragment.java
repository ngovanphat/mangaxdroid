package com.example.mangaxdroid.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.ArrayList;

public class CommentMangaInfoFragment extends Fragment {
    ListView listView;
    Manga manga;
    EditText textComment;
    ImageView btnSend;
    ArrayList<Comment> listComments;
    CommentAdapter adapter;

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
            }
        });
        return view;
    }

    public void loadContent(final String nameManga){
        listComments.clear();
        listComments.add(new Comment("Test comment function ABC\nTest comment function ABC","15/05/2020"));
        listComments.add(new Comment("Test comment function DEF\nTest comment function DEF","15/05/2020"));
        listComments.add(new Comment("Test comment function GHI\nTest comment function GHI","15/05/2020"));
        listComments.add(new Comment("Test comment function JKL\nTest comment function JKL","15/05/2020"));
        listComments.add(new Comment("Test comment function MNO\nTest comment function MNO","15/05/2020"));
    }
}
