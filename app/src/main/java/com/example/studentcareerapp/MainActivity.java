package com.example.studentcareerapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    private Button studentBTN, staffBTN;
    private ImageView imageCareer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Student Career");

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF6200EE"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);



        studentBTN = findViewById(R.id.btnSTUDENT);
        staffBTN = findViewById(R.id.btnSTAFF);
        imageCareer = findViewById(R.id.careerLOGO);

        studentBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.studentcareerapp.LoginActivity.class);
                intent.putExtra("Role",1);
                startActivity(intent);
            }
        });

        staffBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.example.studentcareerapp.LoginActivity.class);
                intent.putExtra("Role",2);
                startActivity(intent);


            }
        });
    }
}