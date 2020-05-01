package com.example.mangaxdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoriesActivity extends Activity {

    private ListView gridView;
    private String[] categoryName;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoryName= getResources().getStringArray(R.array.categories);
        gridView = (ListView) findViewById(R.id.listviewCategory);
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,categoryName);
        gridView.setAdapter(arrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CategoriesActivity.this,position,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
