package com.example.mangaxdroid;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class AdventureCategoryFragment extends Fragment {
    ListView listView;
    ArrayList<Manga> mangaArrayList;
    MangaAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        listView = (ListView) view.findViewById(R.id.listManga);
        mangaArrayList = new ArrayList<>();
        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
        adapter= new MangaAdapter(view.getContext(), R.layout.manga_avatar, mangaArrayList);
        listView.setAdapter(adapter);
        return view;
    }
}
