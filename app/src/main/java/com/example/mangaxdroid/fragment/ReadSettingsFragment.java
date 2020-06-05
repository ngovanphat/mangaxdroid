package com.example.mangaxdroid.fragment;

import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.List;

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
        //NOT APPLICABLE => HIDDEN
        layout.findViewById(R.id.brightnessIcon).setVisibility(View.GONE);
        seekBar.setVisibility(View.GONE);

        LayoutInflater dialogInflater = getLayoutInflater();
        final View dialogView= dialogInflater.inflate(R.layout.report_dialog, null);
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(context,R.style.AlertDialogStyle)
                .setView(dialogView)
                .setTitle("Chọn nội dung để phản hồi")
                .setPositiveButton("Send Report", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();

                        RadioGroup radioGroup=(RadioGroup)dialogView.findViewById(R.id.radioGroup);
                        RadioButton radioButton=(RadioButton)dialogView.findViewById(radioGroup.getCheckedRadioButtonId());
                        EditText details=(EditText)dialogView.findViewById(R.id.detailsEditText);
                        ((OnReadSettingsListener)context).onReportSubmit(radioButton.getText().toString(),details.getText().toString());

                    }
                });
        final AlertDialog choice=alertBuilder.create();
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportBtn.setEnabled(false);
                choice.show();
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.60);
                choice.getWindow().setLayout(width,height);
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
        void onReportSubmit(final String topic,final String details);
    }

}
