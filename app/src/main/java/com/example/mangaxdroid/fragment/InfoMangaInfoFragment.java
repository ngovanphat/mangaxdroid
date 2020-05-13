package com.example.mangaxdroid.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.object.Manga;

public class InfoMangaInfoFragment extends Fragment {
    TextView author;
    TextView status;
    TextView category;
    TextView viewCount;
    TextView description;
    Manga manga;

    public InfoMangaInfoFragment(Manga manga) {
        this.manga = manga;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_info_mangainfo, container, false);
        author = (TextView) view.findViewById(R.id.authorName);
        category = (TextView) view.findViewById(R.id.genres);
        viewCount = (TextView) view.findViewById(R.id.views);
        description = (TextView) view.findViewById(R.id.textviewContent);
        status = (TextView) view.findViewById(R.id.status);

        author.setText(manga.getAuthor());
        category.setText(manga.getCategory());
        viewCount.setText(String.valueOf(manga.getViewCount()));
        description.setText(manga.getDescription());
        return view;
    }
}
