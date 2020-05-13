package com.example.mangaxdroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.example.mangaxdroid.R;

public class ReadChapterSetting extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] viewTypes={"Horizontal","Vertical"};
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_read_chapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sharedPreferences=this.getSharedPreferences("view",MODE_PRIVATE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
