package com.example.mangaxdroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.object.Comment;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Comment> listComment;

    public CommentAdapter(Context context, int layout, ArrayList<Comment> listComment) {
        this.context = context;
        this.layout = layout;
        this.listComment = listComment;
    }

    @Override
    public int getCount() {
        return listComment.size();
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
        ImageView userImage = (ImageView) convertView.findViewById(R.id.userImage);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        TextView userComment = (TextView) convertView.findViewById(R.id.userComment);
        TextView commentDate = (TextView) convertView.findViewById(R.id.commentDate);
        userComment.setText(listComment.get(position).getContent());
        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            commentDate.setText(prettyTime.format(dateFormat.parse(listComment.get(position).getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userName.setText(listComment.get(position).getUserID());
        return convertView;
    }
}

