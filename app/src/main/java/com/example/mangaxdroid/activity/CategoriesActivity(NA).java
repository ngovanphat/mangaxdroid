//package com.example.mangaxdroid;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.Toast;
//import java.util.ArrayList;
//
//public class CategoriesActivity extends Activity {
//    GridView gridViewManga;
//    ArrayList<Manga> mangaArrayList;
//    MangaAdapter mangaAdapter;
//    ArrayAdapter<CharSequence> arrayAdapterCategories;
//    Spinner spinnerCategories;
//    String[] arrayCategories;
//    ImageView imageViewLogoCategory;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_categories);
//        connectContent();
//
//        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(CategoriesActivity.this,arrayCategories[position],Toast.LENGTH_SHORT).show();
//                loadMangaCategories(arrayCategories[position]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        imageViewLogoCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(CategoriesActivity.this,MainActivity.class));
//            }
//        });
//    }
//
//    private void loadMangaCategories(String category){
//        mangaArrayList.clear();
//        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
//        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
//        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
//        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
//        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
//
//        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
//        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
//        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
//        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
//        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
//
//        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
//        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
//        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
//        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
//        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
//
//        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
//        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
//        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
//        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
//        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
//
//        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
//        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
//        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
//        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
//        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
//        mangaAdapter.notifyDataSetChanged();
//    }
//
//    private void connectContent(){
//        arrayCategories =  getResources().getStringArray(R.array.categories);
//        arrayAdapterCategories =  ArrayAdapter.createFromResource(this,R.array.categories ,android.R.layout.simple_spinner_item);
//        arrayAdapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);
//        spinnerCategories.setAdapter(arrayAdapterCategories);
//        gridViewManga = (GridView) findViewById(R.id.gridviewListManga);
//        mangaArrayList = new ArrayList<>();
//
//        mangaArrayList.add(new Manga(R.drawable.image_1,"Truyen 1"));
//        mangaArrayList.add(new Manga(R.drawable.image_2,"Truyen 2"));
//        mangaArrayList.add(new Manga(R.drawable.image_3,"Truyen 3"));
//        mangaArrayList.add(new Manga(R.drawable.image_4,"Truyen 4"));
//        mangaArrayList.add(new Manga(R.drawable.image_5,"Truyen 5"));
//
//        imageViewLogoCategory = (ImageView) findViewById(R.id.imageLogoCategory);
//    }
//}
