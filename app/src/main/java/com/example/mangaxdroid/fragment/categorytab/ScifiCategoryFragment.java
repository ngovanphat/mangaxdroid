package com.example.mangaxdroid.fragment.categorytab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

import com.example.mangaxdroid.activity.MangaInfoActivity;
import com.example.mangaxdroid.object.Manga;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

public class ScifiCategoryFragment extends Fragment {
    ListView listView;
    ArrayList<Manga> mangaArrayList;
    MangaAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) view.findViewById(R.id.listManga);
        mangaArrayList = new ArrayList<>();

        adapter= new MangaAdapter(view.getContext(), R.layout.manga_avatar, mangaArrayList);
        listView.setAdapter(adapter);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/ScifiCategory");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaArrayList.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Manga manga = children.getValue(Manga.class);
                    manga.setId(children.getKey());
                  //  Log.e("manga",manga.getName()+" "+manga.getAuthor()+" "+manga.getCategory()+" "+manga.getViewCount());
                    mangaArrayList.add(manga);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), MangaInfoActivity.class);
                Bundle bundle= new Bundle();
                Manga manga = mangaArrayList.get(position);
                bundle.putSerializable("manga",manga);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }
}
