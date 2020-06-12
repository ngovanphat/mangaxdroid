package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.example.mangaxdroid.adapter.MangaAdapter;
import com.example.mangaxdroid.object.Manga;
import com.example.mangaxdroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class OfflineFragment extends Fragment {
    ListView listView;
    ArrayList<Manga> offlineMangas;
    OfflineMangaAdapter adapter;
    ArrayList<Pair<String,String>> chapter=new ArrayList<>();
    //ArrayList<Pair<Manga,String>> offlineChapters;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.offline, container, false);
        context = view.getContext();
        listView = (ListView) view.findViewById(R.id.listOffline);

      /*  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), ReadChapterActivity.class);
                Bundle bundle = new Bundle();
                Manga manga = offlineMangas.get(position);
                bundle.putSerializable("manga", manga);
                bundle.putString("numberChapter", chapter.get(position).second);
                intent.putExtras(bundle);
                listView.setAdapter(null);
                startActivity(intent);
            }
        });*/
        return view;
    }

    public void getOfflineMangas() {
        final String path = "/Data";
        File f = new File(Environment.getExternalStorageDirectory() +path);
        File[] files = f.listFiles();
        if(files==null)
            return;
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                try{
                    File[] chapterDirs = inFile.listFiles();
                    if(chapterDirs==null)
                        return;
                    for(File chapterDir : chapterDirs) {
                        chapter.add(new Pair(inFile.getName(), chapterDir.getName()));
                    }
                    adapter=new OfflineMangaAdapter(context,R.layout.offline_row,chapter);
                    listView.setAdapter(adapter);
                }catch (Exception e){
                    Log.e("ERROR offline manga", "getOfflineMangas: "+e.getMessage() );
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getOfflineMangas();
    }
    class OfflineMangaAdapter extends BaseAdapter {
        private Context context;
        private int layout;
        ArrayList<Pair<String,String>> chapters;
        //ArrayList<Pair<Manga,String>> offlineChapters;

        public OfflineMangaAdapter(Context context,int layout,ArrayList<Pair<String,String>> chapters){
            this.context=context;
            this.layout=layout;
            this.chapters=chapters;
        }
        @Override
        public int getCount() {
            return chapters.size();
        }

        @Override
        public Object getItem(int position) {
            return chapters.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        private class ViewHolder {
            TextView chapter;
            TextView mangaName;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout, null);
                holder.mangaName=(TextView) convertView.findViewById(R.id.mangaName);
                holder.chapter=(TextView) convertView.findViewById(R.id.chapterNo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Pair<String,String> item=chapters.get(position);
            holder.mangaName.setText(holder.mangaName.getText()+": "+item.first);
            holder.chapter.setText(holder.chapter.getText()+": "+item.second);
            return convertView;
        }
    }
}
