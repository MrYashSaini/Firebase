package com.example.fullfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.fullfirebase.databinding.ActivityDatabaseBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class databaseActivity extends AppCompatActivity {
    FirebaseDatabase database;
    ActivityDatabaseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        DatabaseReference mref = database.getReference().child("user");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseModel model = snapshot.getValue(databaseModel.class);
                binding.tvname.setText(model.getName());
                binding.tvPhoneNo.setText(model.getPhoneNo()+"");
                binding.textView10.setText(model.getWhatsapp()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseModel model = new databaseModel(true,56,binding.etName.getText().toString());
                mref.setValue(model);
            }
        });
    }
}