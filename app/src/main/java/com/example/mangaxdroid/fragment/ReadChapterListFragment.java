package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.CustomChapterListAdapter;
import com.example.mangaxdroid.object.Chapter;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class ReadChapterListFragment extends DialogFragment {
    private static String curViewType;
    private static Manga manga;
    private static String chapterID;
    Context context=null;
    RelativeLayout layout;
    ListView listView;
    ArrayList<Chapter> listChapter;
    CustomChapterListAdapter adapter;
    public static ReadChapterListFragment newInstance(Bundle bundle) {
        ReadChapterListFragment fragment = new ReadChapterListFragment();
        manga = (Manga) bundle.getSerializable("manga");
        chapterID=bundle.getString("chapterID");
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout=(RelativeLayout) inflater.inflate(R.layout.fragment_read_list, container, false);
        listChapter= new ArrayList<>();
        listView = layout.findViewById(R.id.chapterList);
        adapter = new CustomChapterListAdapter(layout.getContext(), R.layout.read_chapter_list_row, listChapter);
        loadContent(manga.getName());
        listView.setAdapter(adapter);

        //listView.smoothScrollToPosition();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((OnReadChapterListListener)context).OnChapterListItemClick(listChapter.get(position).getName());
                ReadChapterListFragment.this.dismiss();
            }
        });
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.80);

        getDialog().getWindow().setLayout(width, height);
    }

    public void loadContent(String nameManga){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Chapters/"+nameManga);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listChapter.clear();
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    listChapter.add(new Chapter(data.getRef().getKey(),"15/05/2020","909"));
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
    public interface OnReadChapterListListener{
        void OnChapterListItemClick(String chapterID);
    }

}
