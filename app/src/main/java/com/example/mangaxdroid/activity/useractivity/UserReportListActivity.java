package com.example.mangaxdroid.activity.useractivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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
import com.google.firebase.database.core.Repo;

import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.i18n.Resources_vi;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserReportListActivity extends AppCompatActivity {
    private ArrayList<Report> reports = new ArrayList<Report>();
    ListView listView;
    Toolbar toolbar;
    ReportAdapter adapter;
    String oldestReportID="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        listView = (ListView) findViewById(R.id.listReports);
        toolbar = (Toolbar) findViewById(R.id.toolBarReports);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView details=(TextView)view.findViewById(R.id.detailsReport);
                if(details.getVisibility()==View.GONE){
                    details.setVisibility(View.VISIBLE);
                }else if (details.getVisibility()==View.VISIBLE){
                    details.setVisibility(View.GONE);
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            @Override
            public void onScrollStateChanged (AbsListView view,int scrollState){
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }
            @Override
            public void onScroll (AbsListView view,int firstVisibleItem,
                                  int visibleItemCount, int totalItemCount){
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
            }
            private void isScrollCompleted () {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    final DatabaseReference reportsDb = FirebaseDatabase.getInstance().getReference("Reports");
                    reportsDb.orderByKey().endAt(oldestReportID).limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Report> temp= new ArrayList<>();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if(!ds.getKey().equals(oldestReportID)){
                                    Report item = ds.getValue(Report.class);
                                    item.setId(ds.getKey());
                                    temp.add(item);
                                }
                            }
                            if(!temp.isEmpty()){
                                oldestReportID = temp.get(0).getId();
                                Collections.reverse(temp);
                                reports.addAll(temp);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
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
    public void getInitialReportList(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final DatabaseReference reportsDb = FirebaseDatabase.getInstance().getReference("Reports");
            reportsDb.orderByKey().limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    reports = new ArrayList<>();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Report item = ds.getValue(Report.class);
                        item.setId(ds.getKey());
                        reports.add(item);
                    }
                    if(!reports.isEmpty()){
                        oldestReportID = reports.get(0).getId();
                        Collections.reverse(reports);
                    }
                    adapter=new ReportAdapter(UserReportListActivity.this,R.layout.report_custom_row,reports);
                    listView.setAdapter(adapter);
                    reportsDb.onDisconnect();
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
        getInitialReportList();
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
            TextView detailsLabel;
            TextView createdAt;
            //TextView phoneModel;
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
                holder.detailsLabel=(TextView)convertView.findViewById(R.id.detailsReportlbl);
                holder.details = (TextView) convertView.findViewById(R.id.detailsReport);
                holder.createdAt = (TextView) convertView.findViewById(R.id.createdAt);
                holder.mangaName=(TextView) convertView.findViewById(R.id.mangaName);
                holder.chapter=(TextView) convertView.findViewById(R.id.chapterId);
                holder.detailsLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.details.setVisibility(View.VISIBLE);
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Report report=reportList.get(position);
            holder.topic.setText(report.getTopic());
            holder.mangaName.setText(report.getManga());
            holder.chapter.setText(report.getChapter());
            holder.details.setText(report.getDetails());
            PrettyTime prettyTime = new PrettyTime();
            String date = prettyTime.format(new Date(Math.abs(report.getCreatedAt())));
            holder.createdAt.setText(String.valueOf(date));
            return convertView;
        }
    }
}
