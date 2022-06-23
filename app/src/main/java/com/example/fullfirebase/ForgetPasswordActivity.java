package com.example.fullfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    Button button;
    EditText email;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().hide();
        button = findViewById(R.id.btnSendForgetEmail);
        auth= FirebaseAuth.getInstance();
        email = findViewById(R.id.ptForgetEmail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ForgetPasswordActivity.this, "Check Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }
}