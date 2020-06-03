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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.ReadChapterActivity;

import org.w3c.dom.Text;

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
        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items={"Chapter không load được","Lỗi giao diện","Chapter không thuộc truyện này","Chapter chưa được dịch"};

                AlertDialog choice=new AlertDialog.Builder(context,R.style.AlertDialogStyle)
                        .setTitle("Choose Topic Of Report")
                        .setSingleChoiceItems(items, 0, null)
                        .setPositiveButton("Send Report", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                // Do something useful withe the position of the selected radio button
                                final Dialog success = new Dialog(context);
                                success.setContentView(R.layout.report_btn_success);
                                final LottieAnimationView successAnimation=success.findViewById(R.id.successAnimationView);
                                final ProgressBar animationProgress=success.findViewById(R.id.progressBar2);
                                final TextView successDialogTxtView=success.findViewById(R.id.successDialogTxtView);
                                successAnimation.addAnimatorListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        successDialogTxtView.setVisibility(View.VISIBLE);
                                        animationProgress.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {
                                    }

                                });
                                success.show();
                                final Handler handler  = new Handler();
                                final Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        if (success.isShowing()) {
                                            success.dismiss();
                                        }
                                    }
                                };
                                success.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        handler.removeCallbacks(runnable);
                                    }
                                });
                                handler.postDelayed(runnable, 4000);



                            }
                        })
                        .show();
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
