package com.example.ass2_i200802_i200707;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    List<ItemModel> featuredItemsList = new ArrayList<>();
    List<ItemModel> yourItemsList = new ArrayList<>();
    ItemAdapter featuredItemsAdapter;
    YourItemAdapter yourItemsAdapter;
    RecyclerView featuredItemsRV, yourItemsRV;
    SqliteHelperHome sqlHelper = new SqliteHelperHome(this);
    String userId;
    ImageView logoutButton;
    User user = CurrentUserDataHolder.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //intialise list
        featuredItemsList = new ArrayList<>();
        yourItemsList = new ArrayList<>();

        //make recycler view orientation horizontal
        featuredItemsRV = findViewById(R.id.featuredItemsRV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        featuredItemsRV.setLayoutManager(layoutManager);

        yourItemsRV = findViewById(R.id.yourItemsRV);
        yourItemsRV.hasFixedSize();
        yourItemsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        //get data from db for recycler view
        // Fetch all users data
        loadData();



        TextView viewAll = findViewById(R.id.viewAllText);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, SearchResults.class);
                startActivity(intent);
            }
        });


        yourItemsRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Item.class);
                startActivity(intent);
            }
        });

        featuredItemsRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Item.class);
                startActivity(intent);
            }
        });


        // Bottom Nav Bar
        Button home = findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Home.class);
                startActivity(intent);
            }
        });

        Button search = findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Search.class);
                intent.putParcelableArrayListExtra("itemList", new ArrayList<>(featuredItemsList));
                startActivity(intent);
            }
        });

        Button addItem = findViewById(R.id.addButton);
        addItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, PostItem.class);
                startActivityForResult(intent,10);
            }
        });

        Button chat = findViewById(R.id.chatButton);
        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Chat.class);
                startActivity(intent);
            }
        });

        Button profile = findViewById(R.id.profileButton);
        profile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Profile.class);

                startActivity(intent);
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });

    }


    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && data != null){

            String imgUrl = data.getStringExtra("itemImg");
            String name = data.getStringExtra("itemName");
            String price = data.getStringExtra("itemPrice");
            String date = data.getStringExtra("itemDate");
            String desc = data.getStringExtra("itemDescription");
            String userId = data.getStringExtra("userId");
            String itemId = data.getStringExtra("itemId");

            ItemModel newItem = new ItemModel(name, price, date, userId, imgUrl, desc);
            newItem.setItemId(itemId);
            featuredItemsList.add(newItem);
            sqlHelper.insertItem(newItem);
            featuredItemsRV.getAdapter().notifyDataSetChanged();

            yourItemsList.add(newItem);;
            yourItemsRV.getAdapter().notifyDataSetChanged();
        }

    }




    private void loadData() {

        // See if connected to internet
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Home", "Connected to Internet");

            yourItemsList.clear();
            featuredItemsList.clear();

            new Home.ItemsGetRequestAsyncTask().execute();

        }
        else {
            Log.d("Home", "Not Connected to Internet");
            Toast.makeText(Home.this, "Fetching data from SQLite", Toast.LENGTH_SHORT).show();

            List<ItemModel> itemList = sqlHelper.getAllItems();

            yourItemsList.clear();
            featuredItemsList.clear();

            featuredItemsList.addAll(itemList);
            Log.d("Request", "Added to from Sqllite" + itemList);

            // Iterate through the featuredItemsList
            for (ItemModel item : featuredItemsList) {
                // Check if the item's userID matches the specified userID
                if (item.getUserId().equals(String.valueOf(user.getUserId()))) {
                    // Add the item to yourItemList
                    yourItemsList.add(item);
                }
            }

            // Update RecyclerView adapter
            featuredItemsAdapter = new ItemAdapter(Home.this, featuredItemsList);
            featuredItemsRV.setAdapter(featuredItemsAdapter);

            //update youritems adapter
            yourItemsAdapter = new YourItemAdapter(Home.this, yourItemsList);
            yourItemsRV.setAdapter(yourItemsAdapter);

        }

    }

    private class ItemsGetRequestAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            initiateItemsGetRequest(new Home.RequestCallback() {
                @Override
                public void onRequestComplete(boolean success) {
                    if (success) {
                        Log.d("Login", "Get Request success: " + success);

                        sqlHelper.deleteAllItems();
                        sqlHelper.insertAllItems(featuredItemsList);

                        // Update RecyclerView adapter
                        featuredItemsAdapter = new ItemAdapter(Home.this, featuredItemsList);
                        featuredItemsRV.setAdapter(featuredItemsAdapter);

                        //update youritems adapter
                        yourItemsAdapter = new YourItemAdapter(Home.this, yourItemsList);
                        yourItemsRV.setAdapter(yourItemsAdapter);
                    }
                    else {
                        Log.d("Login", "Get Request failed: " + success);
                    }
                }
            });
            return null;
        }

    }



    private void initiateItemsGetRequest(Home.RequestCallback callback) {
        Log.d("Home", "Initiating get req");

        String webApiUrl = "http://192.168.1.14/smd_a3/get_items.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, webApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the full response
                       //log.d("Home", "Response: " + response);

                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getInt("Status") == 1) {
                                JSONArray itemsArray = object.getJSONArray("Items");

                                for (int i = 0; i < itemsArray.length(); i++) {
                                    JSONObject itemObject = itemsArray.getJSONObject(i);

                                    String itemId = itemObject.getString("itemId");
                                    String userId = itemObject.getString("userId");
                                    String itemName = itemObject.getString("itemName");
                                    String itemPrice = itemObject.getString("itemPrice");
                                    String itemDescription = itemObject.getString("itemDescription");
                                    String itemDate = itemObject.getString("itemDate");
                                    String imgUrl = itemObject.getString("imgUrl");

                                    ItemModel item = new ItemModel(itemName, itemPrice, itemDate, userId, imgUrl, itemDescription);
                                    item.setItemId(itemId);

                                    featuredItemsList.add(item);

                                    if (Integer.parseInt(userId) == user.getUserId())
                                    {
                                        yourItemsList.add(item);
                                    }
                                }


                                callback.onRequestComplete(true);
                                Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle other status values if needed
                                Log.d("Login", "Get failed with status: " + object.getInt("status"));
                                callback.onRequestComplete(false);
                            }

                        } catch (JSONException e) {
                            // Handle the case where the response is not a JSON object
                            callback.onRequestComplete(false);
                            Log.d("Login", "JSONException: " + e.getMessage());
                            e.printStackTrace(); // Add this line to log the stack trace
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle error
                        Toast.makeText(Home.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Login", "Get fail: " + e.getMessage());
                        callback.onRequestComplete(false);
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(Home.this);
        queue.add(stringRequest);
    }


    private interface RequestCallback {
        void onRequestComplete(boolean success);
    }


}