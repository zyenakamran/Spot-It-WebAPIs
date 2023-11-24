package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    EditText nameTextBox, emailTextBox, contactTextBox;
    TextView saveChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        // Get from Intent
        //User user = getIntent().getParcelableExtra("User");

        nameTextBox = findViewById(R.id.nameTextBox);
        emailTextBox = findViewById(R.id.emailTextBox);
        contactTextBox = findViewById(R.id.contactTextBox);
        saveChangesButton =  findViewById(R.id.saveChangesButton);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameTextBox.getText().toString();
                String email = emailTextBox.getText().toString();
                String contact = contactTextBox.getText().toString();

                // Send data to home
                Intent intent = new Intent();
                intent.putExtra("userName", name);
                intent.putExtra("userEmail", email);
                intent.putExtra("userContact", contact);

                setResult(RESULT_OK, intent);
                finish();
            }
        });



        ImageView back = findViewById(R.id.backArrowIcon);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, Profile.class);
                //intent.putExtra("User", (Parcelable) user);
                startActivity(intent);
            }
        });
    }
}