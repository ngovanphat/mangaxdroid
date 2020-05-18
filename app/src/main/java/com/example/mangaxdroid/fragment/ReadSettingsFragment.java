package com.example.mangaxdroid.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;

public class ReadSettingsFragment extends DialogFragment {
    private static String curViewType;
    final String[] viewTypes={"Horizontal","Vertical"};
    Context context=null;
    RelativeLayout layout;
    Spinner viewType;

    public static ReadSettingsFragment newInstance(Bundle bundle) {
        ReadSettingsFragment fragment = new ReadSettingsFragment();
        curViewType=bundle.getString("currentViewType");
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout=(RelativeLayout) inflater.inflate(R.layout.fragment_read_settings, container, false);
        viewType=layout.findViewById(R.id.viewType);
        final ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,viewTypes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ReadChapterActivity parent=(ReadChapterActivity)getActivity();
        //Setting the ArrayAdapter data on the Spinner
        viewType.setAdapter(aa);
        viewType.setSelection(aa.getPosition(curViewType));
        viewType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    ((OnReadSettingsListener)context).OnReadSettingsChanged(viewType.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //nothing
            }

        });
        return layout;
    }
    public interface OnReadSettingsListener{
        void OnReadSettingsChanged(String setViewType);
    }

}
