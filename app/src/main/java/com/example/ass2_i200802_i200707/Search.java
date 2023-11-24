package com.example.ass2_i200802_i200707;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    EditText searchTextBox;
    ImageView searchIcon;
    // all items
    List<ItemModel> itemList;
    private LinearLayout searchResultsView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        itemList = getIntent().<ItemModel>getParcelableArrayListExtra("itemList");

        searchTextBox = findViewById(R.id.searchTextBox);
        searchIcon = findViewById(R.id.searchBarIcon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });



        // Bottom Nav Bar
        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, Home.class);
                startActivity(intent);
            }
        });

        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, Search.class);
                startActivity(intent);
            }
        });

        Button addItem = findViewById(R.id.addButton);
        addItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, PostItem.class);
                startActivity(intent);
            }
        });

        Button chat = findViewById(R.id.chatButton);
        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, Chat.class);
                startActivity(intent);
            }
        });

        Button profile = findViewById(R.id.profileButton);
        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Search.this, Profile.class);
                startActivity(intent);
            }
        });

    }

    private void performSearch() {
        String query = searchTextBox.getText().toString().toLowerCase();

        // Filter the item list based on the search query
        List<ItemModel> filteredList = filterItemList(query);
        Intent intent = new Intent(this, SearchResults.class);
        intent.putParcelableArrayListExtra("filteredItems", (ArrayList<? extends Parcelable>) new ArrayList<ItemModel>(filteredList));
        startActivity(intent);


    }

    private List<ItemModel> filterItemList(String query) {
        List<ItemModel> filteredList = new ArrayList<>();
        for (ItemModel item : itemList) {
            if (item.getItemName().toLowerCase().contains(query)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }
}

