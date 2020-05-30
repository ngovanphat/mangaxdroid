package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangaxdroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtEmail;
    private Button btnSubmit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        edtEmail = (EditText) findViewById(R.id.editTextSubmitEmail);
        btnSubmit = (Button) findViewById(R.id.buttonSubmitEmail);
        mAuth = FirebaseAuth.getInstance();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAddress = edtEmail.getText().toString().trim();
                if(!emailAddress.equals("")) {
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("ResetPassword", "Email sent.");
                                        //Toast.makeText(ResetPasswordActivity.this, "Vui lòng vào email của bạn để lấy lại mật khẩu", Toast.LENGTH_LONG);
                                        startActivity(new Intent(ResetPasswordActivity.this,MainActivity.class));
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập email của bạn", Toast.LENGTH_LONG);

                }
            }
        });

    }
}
