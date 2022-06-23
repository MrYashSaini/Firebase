package com.example.fullfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.fullfirebase.databinding.ActivityEmailSignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailSignUpActivity extends AppCompatActivity {
    ActivityEmailSignUpBinding binding;
    FirebaseAuth auth;
    boolean passwordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

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


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.ptEmail.getText().toString().isEmpty()) {
                    binding.ptEmail.setError("required");
                } else if (binding.ptPassword.getText().toString().isEmpty()) {
                    binding.ptPassword.setError("required");
                } else {
                    auth.createUserWithEmailAndPassword(binding.ptEmail.getText().toString(), binding.ptPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(EmailSignUpActivity.this, "verify your email", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(EmailSignUpActivity.this, "fail task in verify", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        startActivity(new Intent(EmailSignUpActivity.this, SignInActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(EmailSignUpActivity.this, "error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        binding.tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailSignUpActivity.this,SignInActivity.class));
            }
        });
        binding.tvSignUpPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmailSignUpActivity.this,PhoneSignUpActivity.class));
            }
        });
    }
}