package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity {
    List<ItemModel> ls;
    RecyclerView searchResultsRV;
    SearchResultsItemAdapter adapter;
    FirebaseAuth mAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //get list from home
        ArrayList<ItemModel> filteredItems = getIntent().getParcelableArrayListExtra("filteredItems");

        searchResultsRV = findViewById(R.id.searchResultsRV);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        searchResultsRV.setLayoutManager(layoutManager);

        //set in adapter
        adapter = new SearchResultsItemAdapter(this, filteredItems);
        searchResultsRV.setAdapter(adapter);

        searchResultsRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResults.this, Item.class);
                startActivity(intent);
            }
        });



        ImageView back = findViewById(R.id.backArrow);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchResults.this, Home.class);
                startActivity(intent);
            }
        });


    }
}