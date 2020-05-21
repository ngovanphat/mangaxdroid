package com.example.mangaxdroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mangaxdroid.R;

public class LoginActivity extends Activity {
    EditText email,password;
    Button loginWithEmail,loginWithFacebook,loginWithGoogle;
    TextView forgotPassword,signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }
    private void mapping(){
        email = (EditText) findViewById(R.id.editEmail);
        password = (EditText) findViewById(R.id.editPasword);
        loginWithEmail = (Button) findViewById(R.id.buttonSignIn);
        loginWithFacebook = (Button) findViewById(R.id.buttonFacebook);
        loginWithGoogle = (Button) findViewById(R.id.buttonGoogle);
        forgotPassword = (TextView) findViewById(R.id.forgotPW);
        signUp = (TextView) findViewById(R.id.signUp);
    }

}
