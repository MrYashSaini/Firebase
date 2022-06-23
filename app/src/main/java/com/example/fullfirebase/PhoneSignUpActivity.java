package com.example.fullfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fullfirebase.databinding.ActivityPhoneSignUpBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class PhoneSignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    CountryCodePicker cpp;
    EditText phoneno;
    Button button;
    TextView signbyemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_sign_up);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        cpp = findViewById(R.id.ccp);
        phoneno = findViewById(R.id.ptPhoneNumber);
        button = findViewById(R.id.btnSendOtp);
        signbyemail = findViewById(R.id.tvSignInEmail);
        cpp.registerCarrierNumberEditText(phoneno);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phoneno.getText().toString().isEmpty()){
                    phoneno.setError("required");
                }
                else if(phoneno.getText().toString().length()!=10){
                    phoneno.setError("enter valid number");
                }
                else {
                    Intent intent = new Intent(PhoneSignUpActivity.this, OtpActivity.class);
                    intent.putExtra("mobile", cpp.getFullNumberWithPlus().replace(" ", ""));
                    startActivity(intent);
                }
            }
        });
        signbyemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhoneSignUpActivity.this,EmailSignUpActivity.class));
            }
        });
    }
}