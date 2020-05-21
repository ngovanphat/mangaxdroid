package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    FragmentActivity myContext;
    CarouselView carouselView;
    Button btnViewMore;
    ArrayList<String> imgResource = new ArrayList<>();
    ArrayList<Manga> mangaArrayList = new ArrayList<>();
    ImageView suggestedImage1, suggestedImage2, suggestedImage3, suggestedImage4;
    TextView suggestedText1, suggestedText2, suggestedText3, suggestedText4;
    public static HomeFragment newInstance(){
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        carouselView = view.findViewById(R.id.carouselView);
        loadImgResource();
        loadImgSuggested();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("img", String.valueOf(imgResource.size()));
                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(imgResource.size());
            }
        }, 5000);

        suggestedImage1 = (ImageView) view.findViewById(R.id.suggestedImage1);
        suggestedImage2 = (ImageView) view.findViewById(R.id.suggestedImage2);
        suggestedImage3 = (ImageView) view.findViewById(R.id.suggestedImage3);
        suggestedImage4 = (ImageView) view.findViewById(R.id.suggestedImage4);
        suggestedText1 = (TextView) view.findViewById(R.id.suggestedText1);
        suggestedText2 = (TextView) view.findViewById(R.id.suggestedText2);
        suggestedText3 = (TextView) view.findViewById(R.id.suggestedText3);
        suggestedText4 = (TextView) view.findViewById(R.id.suggestedText4);

        btnViewMore = (Button) view.findViewById(R.id.btnViewMore);
        btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = myContext.getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameMain, CategoriesFragment.newInstance(0));
                transaction.commit();
            }
        });
        return view;
    }

    private void loadImgResource() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/ImageResource/carouselView");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imgResource.clear();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("img",child.getValue(String.class));
                    imgResource.add(child.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadImgSuggested() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/NewCategory");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaArrayList.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Manga manga = children.getValue(Manga.class);
                    mangaArrayList.add(manga);
                }
                Picasso.get().load(mangaArrayList.get(0).getImage()).into(suggestedImage1);
                suggestedText1.setText(mangaArrayList.get(0).getName());
                Picasso.get().load(mangaArrayList.get(1).getImage()).into(suggestedImage2);
                suggestedText2.setText(mangaArrayList.get(1).getName());
                Picasso.get().load(mangaArrayList.get(2).getImage()).into(suggestedImage3);
                suggestedText3.setText(mangaArrayList.get(2).getName());
                Picasso.get().load(mangaArrayList.get(3).getImage()).into(suggestedImage4);
                suggestedText4.setText(mangaArrayList.get(3).getName());
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Picasso.get().load(imgResource.get(position)).into(imageView);
        }
    };
}
