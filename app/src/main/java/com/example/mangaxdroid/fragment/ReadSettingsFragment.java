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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;

public class ReadSettingsFragment extends DialogFragment {
    private static String curViewType;
    final String[] viewTypes={"Horizontal","Vertical"};
    Context context=null;
    RelativeLayout layout;
    Spinner viewType;
    SeekBar seekBar;
    Button reportBtn;
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
        seekBar = layout.findViewById(R.id.brightnessBar);
        reportBtn=layout.findViewById(R.id.reportBtn);
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                WindowManager.LayoutParams systemAtt = getActivity().getWindow().getAttributes();
                systemAtt.screenBrightness = (float)progress;
                getActivity().getWindow().setAttributes(systemAtt);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // called when the user first touches the SeekBar
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // called after the user finishes moving the SeekBar
            }
        });
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.80);

        getDialog().getWindow().setLayout(width, height);
    }

    public interface OnReadSettingsListener{
        void OnReadSettingsChanged(String setViewType);
    }

}
