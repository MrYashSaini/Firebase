package com.example.fullfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.example.fullfirebase.databinding.ActivitySignInBinding;
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
//    private CallbackManager callbackManager;
    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

//        callbackManager = CallbackManager.Factory.create();
//        binding.loginButton.setPermissions(Arrays.asList("email","user_birthday"));
//        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(@NonNull FacebookException e) {
//
//            }
//        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.ptPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right=2;
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(motionEvent.getRawX()>=binding.ptPassword.getRight()-binding.ptPassword.getCompoundDrawables()[Right].getBounds().width()){
                        int selection = binding.ptPassword.getSelectionEnd();
                        if(passwordVisible){
                            binding.ptPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            binding.ptPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }
                        else {
                            binding.ptPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_24,0);
                            binding.ptPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        binding.ptPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        // sign with gmail
        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.ptEmail.getText().toString().isEmpty()) {
                    binding.ptEmail.setError("required");
                } else if (binding.ptPassword.getText().toString().isEmpty()) {
                    binding.ptPassword.setError("required");
                }
                else {
                    auth.signInWithEmailAndPassword(binding.ptEmail.getText().toString(),binding.ptPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (auth.getCurrentUser().isEmailVerified()) {
                                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                                        } else {
                                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        // auto login
        if(auth.getCurrentUser()!=null && auth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
        }
        // go signup activity
        binding.tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,EmailSignUpActivity.class));
            }
        });
        // go phone sign up activity
        binding.tvSignUpPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,PhoneSignUpActivity.class));
            }
        });
        // sign in by google
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        // go forget password activity
        binding.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,ForgetPasswordActivity.class));
            }
        });
    }
    // signIn by google

    int RC_SIGN_IN = 26;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

//    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//        @Override
//        protected void onCurrentAccessTokenChanged(@Nullable AccessToken accessToken, @Nullable AccessToken accessToken1) {
//            if(accessToken1==null){
//                Toast.makeText(SignInActivity.this, "logout", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                loaduserProfile(accessToken1);
//            }
//        }
//    };

//    private void loaduserProfile(AccessToken newAccessToken){
//        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
//                if(jsonObject!=null){
//                    try{
//                        String email = jsonObject.getString("email");
//                        String id = jsonObject.getString("id");
//                        startActivity(new Intent(SignInActivity.this,MainActivity.class));
//                        Toast.makeText(SignInActivity.this, email+"\n"+id, Toast.LENGTH_SHORT).show();
//                    }
//                    catch (JSONException ex){
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        });
//        Bundle parameter = new Bundle();
//        parameter.putString("fields","email,id");
//        request.setParameters(parameter);
//    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }
}