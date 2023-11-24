package com.example.ass2_i200802_i200707;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Rent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        Intent intent = getIntent();
        int price = intent.getIntExtra("itemPrice", 0);
        String userId = intent.getStringExtra("userId");
        String itemName = intent.getStringExtra("itemName");
        String img = intent.getStringExtra("itemImg");


        Button calculateButton = findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText hoursTextBox= findViewById(R.id.hoursTextBox);
                String hours = hoursTextBox.getText().toString();
                int hours_int = 0;

                try {
                    hours_int = Integer.parseInt(hours);

                } catch (NumberFormatException e) {

                }
                int totalCost = price * hours_int;
                String priceString = "$" + totalCost;

                TextView itemPrice = findViewById(R.id.priceText);
                itemPrice.setText(priceString);
            }
        });


        Button rentText= findViewById(R.id.rentText);
        rentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                DatabaseReference itemsRef = rootRef.child("Requests");

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String requesterId = currentUser.getUid();
                RequestModel newReq = new RequestModel(img, userId, requesterId, itemName);
                itemsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            //item id is request id
                            String itemId = itemsRef.push().getKey();
                            itemsRef.child(itemId).setValue(newReq);
                        }
                        else {

                            String itemId = itemsRef.push().getKey();
                            itemsRef.child(itemId).setValue(newReq);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });

                 */


                Toast.makeText(Rent.this,"Request Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Rent.this, Home.class);
                startActivity(intent);


            }
        });


    }
}