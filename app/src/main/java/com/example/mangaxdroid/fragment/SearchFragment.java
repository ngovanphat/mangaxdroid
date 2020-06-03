package com.example.mangaxdroid.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.MangaInfoActivity;
import com.example.mangaxdroid.object.Manga;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class SearchFragment extends Fragment {

    private ArrayList<String> key = new ArrayList<>();
    private HashMap<String,DatabaseReference> idManga = new HashMap<>();
    private List<String> category;
    private ArrayAdapter arrayAdapter;
    private Manga manga;

    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedIntanceState){
        super.onCreate(savedIntanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        SearchView searchView = view.findViewById(R.id.searchview);
        ListView listView = (ListView) view.findViewById(R.id.listSearch);
        loadKey(view);
        arrayAdapter = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1,key);
        listView.setAdapter(arrayAdapter);
        searchView.setActivated(true);
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(key.contains(query)){
                    arrayAdapter.getFilter().filter(query);
                }else{
                    Toast.makeText(view.getContext(),"Từ khóa không tồn tại",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
              final String Name = arrayAdapter.getItem(position).toString();
              if(category.contains(Name)){
                 int pageSelect =  category.indexOf(Name);
                  FragmentTransaction transaction = getFragmentManager().beginTransaction();
                  transaction.replace(R.id.frameMain, CategoriesFragment.newInstance(pageSelect));
                  transaction.commit();
              }
              else if(key.contains(Name)){
                  final Manga manga = loadMangaByName(Name);
                  final Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          if(manga!=null&&manga.getName().equals(Name)){
                              Intent intent = new Intent(view.getContext(), MangaInfoActivity.class);
                              Bundle bundle= new Bundle();
                              bundle.putSerializable("manga",manga);
                              intent.putExtras(bundle);
                              startActivity(intent);
                          }
                      }
                  }, 300);

              }
            }
        });

        return view;
    }
    public void loadKey(final View view){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Data/Mangas/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key.clear();
                String[] categoryList = view.getContext().getResources().getStringArray(R.array.categories);
                category = Arrays.asList(categoryList);
                for (int i = 0; i < categoryList.length; i++) {
                    key.add(categoryList[i]);
                }
                idManga.clear();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    for(DataSnapshot Obj : children.getChildren()){
                        String Name = (String) Obj.child("Name").getValue();
                        key.add(Name);
                        idManga.put(Name,Obj.getRef());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    public Manga loadMangaByName(final String Name){
        DatabaseReference myRef = idManga.get(Name);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                manga = dataSnapshot.getValue(Manga.class);
                manga.setId(dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return manga;
    }
}
