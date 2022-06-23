package com.example.fullfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    Button verify;
    EditText otp;
    String phoneNo;
    FirebaseAuth mAuth;
    String otpId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        verify = findViewById(R.id.btnVerifyOtp);
        otp = findViewById(R.id.ptOtp);
        mAuth = FirebaseAuth.getInstance();
        phoneNo = getIntent().getStringExtra("mobile").toString();
        initiateotp();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp.getText().toString().isEmpty()){
                    Toast.makeText(OtpActivity.this, "enter Otp", Toast.LENGTH_SHORT).show();
                }
                else if(otp.getText().toString().length()!=6){
                    Toast.makeText(OtpActivity.this, "Enter valied otp", Toast.LENGTH_SHORT).show();
                }
                else{
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpId,otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(OtpActivity.this,MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(OtpActivity.this,"error in auto verify",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initiateotp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otpId = s;
                                Toast.makeText(OtpActivity.this, "by code", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                Toast.makeText(OtpActivity.this, "auto verify", Toast.LENGTH_SHORT).show();
                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(OtpActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();

                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}