package com.example.mangaxdroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};
    ImageView btnViewCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        connectContent();
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        btnViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CategoriesActivity.class));
            }
        });
    }

    private void connectContent(){
        carouselView = findViewById(R.id.carouselView);
        btnViewCategory = (ImageView) findViewById(R.id.buttonMenuCategory);

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
            Toast.makeText(MainActivity.this,"Ảnh thứ "+position,Toast.LENGTH_SHORT).show();
        }
    };
    public void toInfo(View v){
        Intent intent = new Intent(MainActivity.this, MangaInfoActivity.class);
        startActivity(intent);
    }
}
