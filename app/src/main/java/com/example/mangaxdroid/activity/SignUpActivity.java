package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPassword,edtConfirmPassword;
    private Button btnSignUp,btnSignInWithFacebook,btnSignInWithGoogle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mapping();
        mAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirm = edtConfirmPassword.getText().toString().trim();
                SignUp(email,password,confirm);
            }
        });
    }

    private void SignUp(String email,String password,String confirm){
        if(email.equals("")||password.equals("")||confirm.equals("")){
            Toast.makeText(SignUpActivity.this,"Vui lòng nhập email và mật khẩu",Toast.LENGTH_LONG).show();
        }else{
            if(!(password.equals(confirm))){
                Toast.makeText(SignUpActivity.this,"Xác nhận mật khẩu không khớp "+password+" "+confirm,Toast.LENGTH_LONG).show();
            }else{
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SignUp", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("SignUp", "createUserWithEmail:failure " + task.getException().getMessage());
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });

            }
        }
    }
    private void updateUI(FirebaseUser user){
        if(user==null){
            
        }else{
            
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    private void mapping(){
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtPassword = (EditText) findViewById(R.id.editPasword);
        edtConfirmPassword = (EditText) findViewById(R.id.editConfirm);
        btnSignUp = (Button) findViewById(R.id.buttonSignIn);
        btnSignInWithFacebook= (Button) findViewById(R.id.buttonFacebook);
        btnSignInWithGoogle = (Button) findViewById(R.id.buttonGoogle);
    }

}
