//package com.example.mangaxdroid.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import com.bumptech.glide.Glide;
//import com.example.mangaxdroid.object.Chapter;
//import com.example.mangaxdroid.adapter.CustomChapterListAdapter;
//import com.example.mangaxdroid.R;
//import com.example.mangaxdroid.object.Manga;
//import java.util.ArrayList;
//
//public class MangaInfoActivity extends Activity {
//    ImageView managaInfoMainLogo;
//    ListView listViewChapter;
//    ArrayList<Chapter> listChapter;
//    TextView author;
//    TextView status;
//    TextView category;
//    TextView viewCount;
//    TextView description;
//    ImageView image;
//    TextView name;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.manga_info);
//        connectContent();
//
//        listViewChapter = (ListView) findViewById(R.id.listViewChapter);
//        listViewChapter.setAdapter(new CustomChapterListAdapter(this, R.layout.chapter_list_custom_row, listChapter));
//        listViewChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(MangaInfoActivity.this, ReadChapterActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        managaInfoMainLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MangaInfoActivity.this, MainActivity.class));
//            }
//        });
//
//        Intent intent = getIntent();
//        Bundle bundle = intent.getExtras();
//        Manga manga = (Manga) bundle.getSerializable("manga");
//
//        name.setText(manga.getName());
//        author.setText(manga.getAuthor());
//        category.setText(manga.getCategory());
//        viewCount.setText(String.valueOf(manga.getViewCount()));
//        description.setText(manga.getDescription());
//        Glide.with(this)
//                .load(manga.getImage())
//                .centerCrop()
//                .into(image);
//    }
//
//    public void readFromStart(View view) {
//        Intent intent = new Intent(MangaInfoActivity.this, ReadChapterActivity.class);
//        startActivity(intent);
//    }
//
//    private void connectContent() {
//        managaInfoMainLogo = (ImageView) findViewById(R.id.managaInfoMainLogo);
//        author = (TextView) findViewById(R.id.authorName);
//        name =(TextView) findViewById(R.id.mangaTitle);
//        category = (TextView) findViewById(R.id.genres);
//        viewCount = (TextView) findViewById(R.id.views);
//        description = (TextView) findViewById(R.id.textviewContent);
//        image = (ImageView) findViewById(R.id.mangaThumbnail);
//        status = (TextView) findViewById(R.id.status);
//        listChapter = new ArrayList<>();
//        listChapter.add(new Chapter("Chapter 1", "01/01/2020", "123456"));
//        listChapter.add(new Chapter("Chapter 2", "01/01/2020", "123456"));
//        listChapter.add(new Chapter("Chapter 3", "01/01/2020", "123456"));
//        listChapter.add(new Chapter("Chapter 4", "01/01/2020", "123456"));
//        listChapter.add(new Chapter("Chapter 5", "01/01/2020", "123456"));
//    }
//}

package com.example.mangaxdroid.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.fragment.MangaInfoFragment;
import com.example.mangaxdroid.object.Manga;

public class MangaInfoActivity extends FragmentActivity {
    ImageView image;
    TextView name;
    FrameLayout frame;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manga_info_new);
        connectContent();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Manga manga = (Manga) bundle.getSerializable("manga");

        name.setText(manga.getName());
        Glide.with(this)
                .load(manga.getImage())
                .fitCenter()
                .into(image);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        MangaInfoFragment MIF = MangaInfoFragment.newInstance();
        transaction.replace(R.id.frameMangaInfo, MIF);
        transaction.commit();

        MIF.onMsgFromMainToFragment(manga);
    }

    private void connectContent() {
        name = (TextView) findViewById(R.id.mangaTitleNew);
        image = (ImageView) findViewById(R.id.mangaThumbnailNew);
        frame = (FrameLayout) findViewById(R.id.frameMangaInfo);
    }
}






