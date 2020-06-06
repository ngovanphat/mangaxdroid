package com.example.mangaxdroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import com.facebook.FacebookSdk;

public class LoginActivity extends Activity {
    private EditText edtEmail,edtPassword;
    private Button loginWithEmail;
    private TextView forgotPassword, signUp;
    private FirebaseAuth mAuth;
    private SignInButton loginWithGoogle;
    private int RC_SIGN_IN = 123;
    private CallbackManager callbackManager;
    private LoginButton loginWithFacebook;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        mAuth = FirebaseAuth.getInstance();
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                finish();
                startActivity(intent);

            }
        });
        loginWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                signInWithEmail(email,password);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        final GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        loginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                SignInWithGoogle(mGoogleSignInClient);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginWithFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FacebookAuthentication", "onSuccess"+loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });
    }

    private void handleFacebookToken(AccessToken accessToken) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressDialog.show();
        Log.d("handleFacebookToken", accessToken.getToken());
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("success","Facebook login success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    uploadUI(user);

                }
                else{
                    Log.d("failed","Facebook login failed"+task.getException().getMessage());
                    uploadUI(null);

                }
            }
        });
    }

    private void SignInWithGoogle(GoogleSignInClient mGoogleSignInClient){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("success", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("failed", "Google sign in failed", e);
                uploadUI(null);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            uploadUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("failed",  task.getException().getMessage());
                             uploadUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signInWithEmail(String email,String password){
        if (email.equals("")||password.equals("")) {
            Toast.makeText(LoginActivity.this,"Vui lòng nhập email và mật khẩu",Toast.LENGTH_LONG).show();
        }
        else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("success", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    uploadUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("failed", "signInWithEmail:failure", task.getException());
                                    uploadUI(null);
                                }
                            }
                        });
            }
    }

    private void mapping(){
        edtEmail = (EditText) findViewById(R.id.editEmail);
        edtPassword = (EditText) findViewById(R.id.editPasword);
        loginWithEmail = (Button) findViewById(R.id.buttonSignIn);
        loginWithFacebook = (LoginButton) findViewById(R.id.buttonFacebook);
        loginWithGoogle = (SignInButton) findViewById(R.id.buttonGoogle);
        forgotPassword = (TextView) findViewById(R.id.forgotPW);
        signUp = (TextView) findViewById(R.id.signUp);
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
    }

    private void uploadUI(FirebaseUser user){
        progressDialog.dismiss();
        if (user == null) {
            Toast.makeText(this,"Đăng nhập thất bại",Toast.LENGTH_LONG).show();
        }
        else {
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            finish();
        }
    }
}
