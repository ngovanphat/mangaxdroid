package com.example.mangaxdroid.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.MangaInfoActivity;
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
    ArrayList<Manga> mangaArrayList2 = new ArrayList<>();
    ArrayList<Manga> mangaArrayList3 = new ArrayList<>();
    ImageView //suggestedImage1, suggestedImage2, suggestedImage3, suggestedImage4,
    stormImage1, stormImage2, stormImage3, stormImage4, stormImage5, stormImage6,
    newImage1, newImage2, newImage3, newImage4, newImage5, newImage6,
    hotImage1, hotImage2, hotImage3, hotImage4, hotImage5, hotImage6;
    TextView //suggestedText1, suggestedText2, suggestedText3, suggestedText4,
    stormText1, stormText2, stormText3, stormText4, stormText5, stormText6,
    newText1, newText2, newText3, newText4, newText5, newText6,
    hotText1, hotText2, hotText3, hotText4, hotText5, hotText6,
    toNew, toHot;
    LinearLayout storm1, storm2, storm3, storm4, storm5, storm6,
    new1, new2, new3, new4, new5, new6,
    hot1, hot2, hot3, hot4, hot5, hot6;
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
        //loadImgSuggested();
        loadImgStorm();
        loadImgNew();
        loadImgHot();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("img", String.valueOf(imgResource.size()));
                carouselView.setImageListener(imageListener);
                carouselView.setPageCount(imgResource.size());
            }
        }, 5000);

//        suggestedImage1 = (ImageView) view.findViewById(R.id.suggestedImage1);
//        suggestedImage2 = (ImageView) view.findViewById(R.id.suggestedImage2);
//        suggestedImage3 = (ImageView) view.findViewById(R.id.suggestedImage3);
//        suggestedImage4 = (ImageView) view.findViewById(R.id.suggestedImage4);
        stormImage1 = (ImageView) view.findViewById(R.id.stormImage1);
        stormImage2 = (ImageView) view.findViewById(R.id.stormImage2);
        stormImage3 = (ImageView) view.findViewById(R.id.stormImage3);
        stormImage4 = (ImageView) view.findViewById(R.id.stormImage4);
        stormImage5 = (ImageView) view.findViewById(R.id.stormImage5);
        stormImage6 = (ImageView) view.findViewById(R.id.stormImage6);
        newImage1 = (ImageView) view.findViewById(R.id.newImage1);
        newImage2 = (ImageView) view.findViewById(R.id.newImage2);
        newImage3 = (ImageView) view.findViewById(R.id.newImage3);
        newImage4 = (ImageView) view.findViewById(R.id.newImage4);
        newImage5 = (ImageView) view.findViewById(R.id.newImage5);
        newImage6 = (ImageView) view.findViewById(R.id.newImage6);
        hotImage1 = (ImageView) view.findViewById(R.id.hotImage1);
        hotImage2 = (ImageView) view.findViewById(R.id.hotImage2);
        hotImage3 = (ImageView) view.findViewById(R.id.hotImage3);
        hotImage4 = (ImageView) view.findViewById(R.id.hotImage4);
        hotImage5 = (ImageView) view.findViewById(R.id.hotImage5);
        hotImage6 = (ImageView) view.findViewById(R.id.hotImage6);
//        suggestedText1 = (TextView) view.findViewById(R.id.suggestedText1);
//        suggestedText2 = (TextView) view.findViewById(R.id.suggestedText2);
//        suggestedText3 = (TextView) view.findViewById(R.id.suggestedText3);
//        suggestedText4 = (TextView) view.findViewById(R.id.suggestedText4);
        stormText1 = (TextView) view.findViewById(R.id.stormText1);
        stormText2 = (TextView) view.findViewById(R.id.stormText2);
        stormText3 = (TextView) view.findViewById(R.id.stormText3);
        stormText4 = (TextView) view.findViewById(R.id.stormText4);
        stormText5 = (TextView) view.findViewById(R.id.stormText5);
        stormText6 = (TextView) view.findViewById(R.id.stormText6);
        newText1 = (TextView) view.findViewById(R.id.newText1);
        newText2 = (TextView) view.findViewById(R.id.newText2);
        newText3 = (TextView) view.findViewById(R.id.newText3);
        newText4 = (TextView) view.findViewById(R.id.newText4);
        newText5 = (TextView) view.findViewById(R.id.newText5);
        newText6 = (TextView) view.findViewById(R.id.newText6);
        hotText1 = (TextView) view.findViewById(R.id.hotText1);
        hotText2 = (TextView) view.findViewById(R.id.hotText2);
        hotText3 = (TextView) view.findViewById(R.id.hotText3);
        hotText4 = (TextView) view.findViewById(R.id.hotText4);
        hotText5 = (TextView) view.findViewById(R.id.hotText5);
        hotText6 = (TextView) view.findViewById(R.id.hotText6);
        storm1 = (LinearLayout) view.findViewById(R.id.storm1);
        storm2 = (LinearLayout) view.findViewById(R.id.storm2);
        storm3 = (LinearLayout) view.findViewById(R.id.storm3);
        storm4 = (LinearLayout) view.findViewById(R.id.storm4);
        storm5 = (LinearLayout) view.findViewById(R.id.storm5);
        storm6 = (LinearLayout) view.findViewById(R.id.storm6);
        new1 = (LinearLayout) view.findViewById(R.id.new1);
        new2 = (LinearLayout) view.findViewById(R.id.new2);
        new3 = (LinearLayout) view.findViewById(R.id.new3);
        new4 = (LinearLayout) view.findViewById(R.id.new4);
        new5 = (LinearLayout) view.findViewById(R.id.new5);
        new6 = (LinearLayout) view.findViewById(R.id.new6);
        hot1 = (LinearLayout) view.findViewById(R.id.hot1);
        hot2 = (LinearLayout) view.findViewById(R.id.hot2);
        hot3 = (LinearLayout) view.findViewById(R.id.hot3);
        hot4 = (LinearLayout) view.findViewById(R.id.hot4);
        hot5 = (LinearLayout) view.findViewById(R.id.hot5);
        hot6 = (LinearLayout) view.findViewById(R.id.hot6);

        toNew = (TextView) view.findViewById(R.id.toNew);
        toNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameMain, CategoriesFragment.newInstance(1));
                transaction.commit();
            }
        });

        toHot = (TextView) view.findViewById(R.id.toHot);
        toHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frameMain, CategoriesFragment.newInstance(0));
                transaction.commit();
            }
        });

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
                    //Log.d("img",child.getValue(String.class));
                    imgResource.add(child.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    private void loadImgSuggested() {
//        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/HotCategory");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mangaArrayList.clear();
//                for (DataSnapshot children : dataSnapshot.getChildren()) {
//                    Manga manga = children.getValue(Manga.class);
//                    mangaArrayList.add(manga);
//                }
//                final Manga manga = mangaArrayList.get(6);
//                Picasso.get().load(mangaArrayList.get(6).getImage()).into(suggestedImage1);
//                suggestedText1.setText(mangaArrayList.get(6).getName());
//                suggested1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
//                        Bundle bundle= new Bundle();
//                        bundle.putSerializable("manga", manga);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                });
//
//                Picasso.get().load(mangaArrayList.get(7).getImage()).into(suggestedImage2);
//                suggestedText2.setText(mangaArrayList.get(7).getName());
//                Picasso.get().load(mangaArrayList.get(8).getImage()).into(suggestedImage3);
//                suggestedText3.setText(mangaArrayList.get(8).getName());
//                Picasso.get().load(mangaArrayList.get(9).getImage()).into(suggestedImage4);
//                suggestedText4.setText(mangaArrayList.get(9).getName());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//            }
//        });
//    }

    private void loadImgStorm() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/HotCategory");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaArrayList.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Manga manga = children.getValue(Manga.class);
                    try {
                        manga.setId(children.getKey());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mangaArrayList.add(manga);
                }
                Picasso.get().load(mangaArrayList.get(6).getImage()).into(stormImage1);

                stormText1.setText(mangaArrayList.get(6).getName());
                storm1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList.get(6));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList.get(7).getImage()).into(stormImage2);
                stormText2.setText(mangaArrayList.get(7).getName());
                storm2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList.get(7));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList.get(8).getImage()).into(stormImage3);
                stormText3.setText(mangaArrayList.get(8).getName());
                storm3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList.get(8));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                Picasso.get().load(mangaArrayList.get(9).getImage()).into(stormImage4);
                stormText4.setText(mangaArrayList.get(9).getName());
                storm4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList.get(9));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList.get(4).getImage()).into(stormImage5);
                stormText5.setText(mangaArrayList.get(4).getName());
                storm5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList.get(4));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList.get(5).getImage()).into(stormImage6);
                stormText6.setText(mangaArrayList.get(5).getName());
                storm6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList.get(5));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    private void loadImgHot() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/HotCategory");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaArrayList2.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Manga manga = children.getValue(Manga.class);
                    try{
                    manga.setId(children.getKey());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    mangaArrayList2.add(manga);
                }
                Picasso.get().load(mangaArrayList2.get(0).getImage()).into(hotImage1);
                hotText1.setText(mangaArrayList2.get(0).getName());
                hot1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList2.get(0));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList2.get(1).getImage()).into(hotImage2);
                hotText2.setText(mangaArrayList2.get(1).getName());
                hot2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList2.get(1));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList2.get(2).getImage()).into(hotImage3);
                hotText3.setText(mangaArrayList2.get(2).getName());
                hot3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList2.get(2));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList2.get(3).getImage()).into(hotImage4);
                hotText4.setText(mangaArrayList2.get(3).getName());
                hot4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList2.get(3));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList2.get(4).getImage()).into(hotImage5);
                hotText5.setText(mangaArrayList2.get(4).getName());
                hot5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList2.get(4));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList2.get(5).getImage()).into(hotImage6);
                hotText6.setText(mangaArrayList2.get(5).getName());
                hot6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList2.get(5));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void loadImgNew() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/NewCategory");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mangaArrayList3.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Manga manga = children.getValue(Manga.class);
                    try{
                        manga.setId(children.getKey());
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    mangaArrayList3.add(manga);
                }

                Picasso.get().load(mangaArrayList3.get(0).getImage()).into(newImage1);
                newText1.setText(mangaArrayList3.get(0).getName());
                new1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList3.get(0));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList3.get(1).getImage()).into(newImage2);
                newText2.setText(mangaArrayList3.get(1).getName());
                new2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList3.get(1));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList3.get(2).getImage()).into(newImage3);
                newText3.setText(mangaArrayList3.get(2).getName());
                new3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList3.get(2));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList3.get(3).getImage()).into(newImage4);
                newText4.setText(mangaArrayList3.get(3).getName());
                new4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList3.get(3));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList3.get(4).getImage()).into(newImage5);
                newText5.setText(mangaArrayList3.get(4).getName());
                new5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList3.get(4));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                Picasso.get().load(mangaArrayList3.get(5).getImage()).into(newImage6);
                newText6.setText(mangaArrayList3.get(5).getName());
                new6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(myContext, MangaInfoActivity.class);
                        Bundle bundle= new Bundle();
                        bundle.putSerializable("manga", mangaArrayList3.get(5));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

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
