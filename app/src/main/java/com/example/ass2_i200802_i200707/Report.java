package com.example.ass2_i200802_i200707;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report extends AppCompatActivity {

    String userId, itemId;
    RatingBar rating;
    EditText review;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Extract from intent
        userId = getIntent().getStringExtra("userId");
        itemId = getIntent().getStringExtra("itemId");

        ImageView back= findViewById(R.id.backArrowIcon);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        review = findViewById(R.id.reportReasonBox);
        rating = findViewById(R.id.rating);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rate =String.valueOf(rating.getRating());
                Toast.makeText(Report.this, rate, Toast.LENGTH_SHORT).show();
            }
        });

        Button submitButton = findViewById(R.id.submitReportButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Report.this, "Review & Rating Submitted", Toast.LENGTH_LONG).show();

                //Add to db

                /*
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference reviewRef = rootRef.child("Reviews");

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                float rate = rating.getRating();
                String rev = review.getText().toString();

                Review newReview = new Review(rev, userId, itemId, rate);

                reviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String reviewId = reviewRef.push().getKey();
                        newReview.setReviewId(reviewId);
                        reviewRef.child(itemId).setValue(newReview);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

                 */

                finish();
            }
        });
    }

}