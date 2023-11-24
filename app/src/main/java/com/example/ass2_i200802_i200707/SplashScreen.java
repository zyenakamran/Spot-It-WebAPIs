package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth mAuth;
    private static final long SPLASH_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(SplashScreen.this, Home.class);
            startActivity(intent);
            finish();
        }
        else
        {
            // Use a Handler to post a delayed action to navigate to the login screen
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Create an Intent to navigate to the login activity
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish(); // Finish the splash activity so the user can't go back to it
                }
            }, SPLASH_DURATION);
        }

    }
}