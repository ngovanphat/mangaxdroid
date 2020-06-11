package com.example.mangaxdroid.activity;

import androidx.annotation.NonNull;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mangaxdroid.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends Activity {
    private FirebaseAuth mAuth;
    private EditText loginName,edtEmail,edtPassword,edtConfirmPassword;
    private Button btnSignUp;
    private TextView signIn;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mapping();
        mAuth = FirebaseAuth.getInstance();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = loginName.getText().toString().trim();
                progressDialog.show();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String confirm = edtConfirmPassword.getText().toString().trim();
                SignUp(name, email,password,confirm);
            }
        });
    }

    private void SignUp(final String name, String email, String password, String confirm){
        if (email.equals("")||password.equals("")||confirm.equals("")) {
            Toast.makeText(SignUpActivity.this,"Vui lòng nhập email và mật khẩu",Toast.LENGTH_LONG).show();
        }
        else {
            if (!(password.equals(confirm))) {
                Toast.makeText(SignUpActivity.this,"Xác nhận mật khẩu không khớp "+password+" "+confirm,Toast.LENGTH_LONG).show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("SignUp", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    final DatabaseReference userdb = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                                    userdb.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            userdb.child("UserName").setValue(name);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    userdb.onDisconnect();
                                    updateUI(user);
                                }
                                else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("SignUp", "createUserWithEmail:failure " + task.getException().getMessage());
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        progressDialog.dismiss();
        if (user == null) {
            Toast.makeText(this,"Đăng ký thất bại",Toast.LENGTH_LONG).show();
        }
        else {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("EmailResult", "Email sent.");
                            }
                        }
                    });
            if(!user.isEmailVerified()){
                Toast.makeText(this,"Vui lòng xác nhận email !",Toast.LENGTH_LONG).show();

            }
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

    }

    private void mapping() {
        loginName = (EditText) findViewById(R.id.loginName);
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtPassword = (EditText) findViewById(R.id.editPasword);
        edtConfirmPassword = (EditText) findViewById(R.id.editConfirm);
        btnSignUp = (Button) findViewById(R.id.buttonSignIn);
        signIn = (TextView) findViewById(R.id.signIn);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }
}
