package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Photo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Button back = findViewById(R.id.exitButton);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button photo = findViewById(R.id.photoButton);
        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Photo.this, Photo.class);
                startActivity(intent);
            }
        });

        Button vid = findViewById(R.id.videoButton);
        vid.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Photo.this, Video.class);
                startActivity(intent);
            }
        });
    }
}