package com.example.mangaxdroid.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.mangaxdroid.R;
import com.example.mangaxdroid.activity.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends DialogFragment {
    Context context = null;
    EditText ogPasword, newPassword, confirmNewPassword;
    Button save;
    ProgressDialog progressDialog;
    ChangePassword thisFragment = this;
    private FirebaseUser user;

    public static ChangePassword newInstance() {
        ChangePassword fragment = new ChangePassword();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_change_password, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        ogPasword = (EditText) view.findViewById(R.id.ogPassword);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        confirmNewPassword = (EditText) view.findViewById(R.id.confirmNewPassword);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        save = (Button) view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = user.getEmail();
                final String oldPass = ogPasword.getText().toString().trim();
                final String newPass = newPassword.getText().toString().trim();
                final String confirm = confirmNewPassword.getText().toString().trim();
                if (oldPass.equals("") || newPass.equals("") || confirm.equals("")) {
                    Toast.makeText(context,"Vui lòng nhập đầy đủ thông tin !",Toast.LENGTH_LONG).show();
                }
                else {
                    if (!(newPass.equals(confirm))) {
                        Toast.makeText(context,"Xác nhận mật khẩu không khớp !",Toast.LENGTH_LONG).show();
                    }
                    else {
                        progressDialog.show();
                        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPass);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(context,"Đổi mật khẩu không thành công. Vui lòng thử lại sau !",Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
                                            }
                                            else {
                                                Toast.makeText(context,"Đổi mật khẩu thành công !",Toast.LENGTH_LONG).show();
                                                progressDialog.dismiss();
                                                getActivity().getSupportFragmentManager().beginTransaction().remove(thisFragment).commit();
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(context,"Xác nhận người dùng thất bại !",Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = (int)(getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.45);
        getDialog().getWindow().setLayout(width, height);
    }
}
