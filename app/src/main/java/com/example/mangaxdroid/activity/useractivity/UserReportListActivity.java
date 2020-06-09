package com.example.mangaxdroid.activity.useractivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.object.Manga;
import com.example.mangaxdroid.object.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserReportListActivity extends AppCompatActivity {
    private ArrayList<Report> reports = new ArrayList<Report>();
    ListView listView;
    Toolbar toolbar;
    ReportAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        listView = (ListView) findViewById(R.id.listFavorites);
        toolbar = (Toolbar) findViewById(R.id.toolBarFavorite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show details
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getReportList(){
        reports = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final AtomicBoolean flag=new AtomicBoolean(false);
            final DatabaseReference rolesDb = FirebaseDatabase.getInstance().getReference("Roles");
            rolesDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild(user.getUid())) {
                        if (snapshot.child(user.getUid()).getValue().equals("Admin")) {
                            flag.set(true);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            final ArrayList<String> mangaListIds = new ArrayList<String>();
            final DatabaseReference reportsDb = FirebaseDatabase.getInstance().getReference("Reports");
            final Query reportsQuery=reportsDb.orderByChild("createdAt");
            reportsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(flag.get())
                        return;
                    reports.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        reports.add(ds.getValue(Report.class));
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            reportsDb.onDisconnect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public class ReportAdapter extends BaseAdapter {
        private Context context;
        private int layout;
        private ArrayList<Report> reportList;
        private ArrayList<String> listChapter = new ArrayList<>();

        public ReportAdapter(Context context, int layout, ArrayList<Report> reportList) {
            this.context = context;
            this.layout = layout;
            this.reportList = reportList;
        }

        @Override
        public int getCount() {
            return reportList.size();
        }

        @Override
        public Object getItem(int position) {
            return reportList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public String getItemID(int position) {
            return reportList.get(position).getId();
        }

        private class ViewHolder {
            TextView topic;
            TextView details;
            TextView createdAt;
            TextView phoneModel;
            TextView mangaName;
            TextView chapter;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(layout, null);
                holder.topic= (TextView) convertView.findViewById(R.id.topicReport);
                holder.details = (TextView) convertView.findViewById(R.id.detailsReport);
                holder.createdAt = (TextView) convertView.findViewById(R.id.createdAt);
                holder.mangaName=(TextView) convertView.findViewById(R.id.mangaName);
                holder.chapter=(TextView) convertView.findViewById(R.id.chapterId);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Report report=reportList.get(position);
            holder.topic.setText(report.getTopic());
            //        holder.categoryManga.setText(manga.getCategory());
            holder.details.setText(report.getDetails());
            holder.createdAt.setText(report.getCreatedAt().toString());
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/ActionCategory");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        Manga manga = children.getValue(Manga.class);
                        manga.setId(children.getKey());
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            return convertView;
        }
    }
}
