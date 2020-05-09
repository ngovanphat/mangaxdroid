package com.example.mangaxdroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity<DatabaseReference> extends Activity {
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R.drawable.image_5};
    ImageView btnViewCategory;
    com.google.firebase.database.DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] categories = getResources().getStringArray(R.array.categories);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("message").addValueEventListener(new ValueEventListener() {
            @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Toast.makeText(MainActivity.this,dataSnapshot.toString(),Toast.LENGTH_LONG).show();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
        });
        connectContent();

        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);


    }

    private void connectContent(){
        carouselView = findViewById(R.id.carouselView);
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    public void toInfo(View v){
        Intent intent = new Intent(MainActivity.this, MangaInfoActivity.class);
        startActivity(intent);
    }
}
