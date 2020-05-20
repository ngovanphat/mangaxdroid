package com.example.mangaxdroid.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;

public class ReadChapterListFragment extends DialogFragment {
    private static String curViewType;
    Context context=null;
    RelativeLayout layout;

    public static ReadChapterListFragment newInstance(Bundle bundle) {
        ReadChapterListFragment fragment = new ReadChapterListFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout=(RelativeLayout) inflater.inflate(R.layout.fragment_read_list, container, false);

        return layout;
    }
    public interface OnReadSettingsListener{
        void OnReadSettingsChanged(String setViewType);
    }

}
