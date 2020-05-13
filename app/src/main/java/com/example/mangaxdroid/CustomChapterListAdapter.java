package com.example.mangaxdroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomChapterListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Chapter> listChapter;

    public CustomChapterListAdapter(Context context, int layout, ArrayList<Chapter> listChapter) {
        this.context = context;
        this.layout = layout;
        this.listChapter = listChapter;
    }

    @Override
    public int getCount() {
        return listChapter.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(layout,null);
        TextView chapterNo = (TextView) convertView.findViewById(R.id.chapterNo);
        TextView dateUpdate = (TextView) convertView.findViewById(R.id.dateUpdate);
        TextView viewCount = (TextView) convertView.findViewById(R.id.viewCount);
        chapterNo.setText(listChapter.get(position).getName());
        dateUpdate.setText(listChapter.get(position).getDate());
        viewCount.setText(listChapter.get(position).getView());
        return convertView;
    }
}

