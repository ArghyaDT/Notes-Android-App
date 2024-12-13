package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.myapplication16.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();

                if(currentUser==null){
                    startActivity(new Intent(Splash.this,Login.class));
                }
                else {
                    startActivity(new Intent(Splash.this,MainActivity.class));
                }
                finish();
            }
        },2000);
    }
}